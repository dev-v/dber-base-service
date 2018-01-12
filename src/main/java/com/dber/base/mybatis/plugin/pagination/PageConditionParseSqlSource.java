package com.dber.base.mybatis.plugin.pagination;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <li>文件名称: PageConditionParseSqlSource.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月20日
 * @author dev-v
 */
public class PageConditionParseSqlSource implements SqlSource {

	private static final ThreadLocal<PageConditionParseSqlSource> SQ_THREAD_LOCAL = new ThreadLocal<>();

	BoundSql boundSql;

	private PageConditionParseSqlSource(BoundSql boundSql) {
		this.boundSql = boundSql;
	}

	public static final PageConditionParseSqlSource getPageConditionParseSqlSource(BoundSql boundSql) {
		PageConditionParseSqlSource sqlSource = SQ_THREAD_LOCAL.get();
		if (sqlSource == null) {
			sqlSource = new PageConditionParseSqlSource(boundSql);
			SQ_THREAD_LOCAL.set(sqlSource);
		} else {
			sqlSource.boundSql = boundSql;
		}
		return sqlSource;
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
		return boundSql;
	}
}
