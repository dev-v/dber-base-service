package com.dber.base.mybatis.plugin.pagination;

import com.dber.base.mybatis.plugin.pagination.dialect.AbstractDialect;
import com.dber.base.mybatis.plugin.pagination.dialect.MysqlDialect;
import com.dber.base.mybatis.plugin.pagination.ext.CacheCount;
import com.dber.base.mybatis.plugin.pagination.ext.PageConditionParseSqlSource;
import com.dber.base.mybatis.plugin.pagination.ext.PageParameterHandler;
import com.dber.base.result.Page;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <li>文件名称: PaginationInterceptor.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月20日
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor implements Interceptor {
    private static final Interceptor instance = new PaginationInterceptor();

    private static final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAMETER_INDEX = 1;
    private static final int ROWBOUNDS_INDEX = 2;
    private static final String sql = "sql";
    private static final String SQLSOURCE_STRING = "sqlSource";
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    private static AbstractDialect dialect = new MysqlDialect();

    private static final Map<String, MappedStatement> pageMappedStatement = new HashMap<>();

    /**
     * <pre>
     * 一、缓存的策略：
     * 1、新查询的count后，若allpage>1则进行缓存
     * 二、使用缓存（缓存数据若返回-1表示没有缓存）
     * 首先检查缓存
     * 2、有缓存直接使用缓存
     * （1）查询页为缓存最大页则当做没有缓存
     * （2）查询数据，数据量等于每页最大查询数据直接返回
     * （3）数据量小于最大查询数据，本地计算count重新缓存
     * （4）数据量为0，则当做没有缓存重走查询流程
     * 没有缓存
     * （1）查询数据库count并进行缓存
     * （2）查询數據
     * 三、缓存失效策略
     * 在进行设置缓存时，不符合条件的不管缓存中存不存在，直接失效
     * </pre>
     */
    public Object intercept(final Invocation invocation) throws Throwable {
        final Object[] queryArgs = invocation.getArgs();
        final Object prameterObj = queryArgs[PARAMETER_INDEX];

        if (prameterObj instanceof Page<?>) {
            final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];

            Page<?> page = (Page<?>) prameterObj;

            final BoundSql boundSql = ms.getBoundSql(page.getCondition());

            String key = PageParameterHandler.parseKeyAndParameters(ms, boundSql);

            long count = CacheCount.getCacheCount(key);

            if (count != -1) {// 使用緩存
                page.setCount(count);
                if (page.getCurrentPage() < page.getAllPage()) {
                    setDatas(queryArgs, page, ms, boundSql, invocation);
                    int size = page.getDatas().size();
                    if (size == page.getPageSize()) {// 緩存有效
                        page.setCount(count);
                        return page.getDatas();
                    } else if (size < page.getPageSize()) {// 本地重置緩存
                        page.setCount(size + (page.getCurrentPage() - 1) * page.getPageSize());
                        CacheCount.cacheCount(key, page);
                        return page.getDatas();
                    }
                }
            }

            // 下面是緩存失效的情況
            Connection connection = ((Executor) invocation.getTarget()).getTransaction().getConnection();
            count = getDataBaseCount(connection, boundSql, page, ms);
            page.setCount(count);
            CacheCount.cacheCount(key, page);

            if (count == 0) {
                page.emptyDatas();
            } else {
                setDatas(queryArgs, page, ms, boundSql, invocation);
            }
            return page.getDatas();
        }

        return invocation.proceed();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private final void setDatas(Object[] queryArgs, Page<?> page, MappedStatement ms, BoundSql boundSql,
                                Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        String id = ms.getId();
        PageConditionParseSqlSource sqlSource = PageConditionParseSqlSource.getPageConditionParseSqlSource(boundSql);

        MappedStatement pms = pageMappedStatement.get(id);
        if (pms == null) {
            Builder builder = getBuilder(ms, sqlSource);
            pms = builder.build();
            pageMappedStatement.put(ms.getId(), pms);
        }

        MetaObject.forObject(boundSql, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY)
                .setValue(sql, dialect.getPageSql(boundSql.getSql(), page));

        MetaObject.forObject(pms, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY)
                .setValue(SQLSOURCE_STRING, sqlSource);

        queryArgs[MAPPED_STATEMENT_INDEX] = pms;
        queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
        queryArgs[PARAMETER_INDEX] = page.getCondition();
        page.setDatas((Collection) invocation.proceed());
    }

    private final Builder getBuilder(MappedStatement ms, SqlSource sqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), sqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(',');
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        builder.timeout(ms.getTimeout());

        builder.parameterMap(ms.getParameterMap());

        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder;
    }

    private final long getDataBaseCount(Connection connection, BoundSql boundSql, Page<?> page, MappedStatement ms)
            throws SQLException {
        long count = 0;

        String countSql = AbstractDialect.getCountSql(boundSql.getSql());

        final PreparedStatement countStmt = connection.prepareStatement(countSql);
        PageParameterHandler.setParameters(countStmt, boundSql);

        final ResultSet rs = countStmt.executeQuery();
        if (rs.next()) {
            count = rs.getInt(1);
        }
        rs.close();
        countStmt.close();

        return count;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private PaginationInterceptor() {
    }

    public static final Interceptor getInstance() {
        return instance;
    }
}
