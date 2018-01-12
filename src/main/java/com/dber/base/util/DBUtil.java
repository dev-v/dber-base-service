package com.dber.base.util;

import javax.sql.DataSource;

import com.dber.config.DbType;
import com.dber.config.JdbcPoolConfig;
import com.dber.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * <li>文件名称: DBUtil.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月20日
 * @author dev-v
 */
public class DBUtil {

	private static final Log LOG = LogFactory.getLog(DBUtil.class);

	static {
		System.getProperties().put("druid.mysql.usePingMethod", "false");
	}

	public static final DataSource dataSource(JdbcPoolConfig config) {
		String url = generateUrl(config);
		return getDruidSource(url, config);
	}

	/**
	 * <pre>
	 * 仅仅支持mysql
	 * </pre>
	 * 
	 * @param config
	 * @return
	 */
	private static String generateUrl(JdbcPoolConfig config) {
		if (DbType.mysql == config.getDbType()) {
			// &useCursorFetch=true",
			StringBuilder sb = new StringBuilder("jdbc:mysql://");
			sb.append(config.getHost()).append(':');
			sb.append(config.getPort()).append('/');
			sb.append(config.getDatabase()).append('?');
			if (Util.isBlank(config.getParameters())) {
				sb.append("characterEncoding=utf8&rewriteBatchedStatements=true&autoReconnect=true");
			} else {
				sb.append(config.getParameters());
			}

			return sb.toString();
		} else {
			UnsupportedOperationException exception = new UnsupportedOperationException(
					"数据库类型暂时不支持：" + config.getDbType());
			LOG.error(exception);
			throw exception;
		}
	}

	public static final DataSource getDruidSource(String url, JdbcPoolConfig config) {
		DruidDataSource dataSource = new DruidDataSource();

		dataSource.setUrl(url);
		dataSource.setUsername(config.getUsername());
		dataSource.setPassword(config.getPassword());

		dataSource.setInitialSize(config.getInitialSize());
		dataSource.setMaxActive(config.getMaxActive());
		dataSource.setMinIdle(config.getMinIdle());
		dataSource.setMaxWait(config.getMaxWait());
		dataSource.setQueryTimeout(config.getQueryTimeOut());

		dataSource.setMaxOpenPreparedStatements(config.getMaxOpenPreparedStatements());

		dataSource.setTestOnBorrow(true);
		dataSource.setTestOnReturn(false);
		dataSource.setTestWhileIdle(false);

		return dataSource;
	}

	// public static final DataSource getC3poSource(String url, String username,
	// String password, String driverClass) {
	// ComboPooledDataSource dataSource = new ComboPooledDataSource();
	//
	// dataSource.setJdbcUrl(url);
	// dataSource.setUser(username);
	// dataSource.setPassword(password);
	// try {
	// dataSource.setDriverClass(driverClass);
	// } catch (PropertyVetoException e) {
	// LOG.error(e);
	// }
	//
	// dataSource.setMaxConnectionAge(60 * 60);
	// dataSource.setMaxIdleTime(60 * 60);
	//
	// return dataSource;
	// }
	//
	// public static final DataSource getNoPool(String url, String username, String
	// password, String driverClass) {
	// DriverManagerDataSource dataSource = new DriverManagerDataSource();
	// dataSource.setJdbcUrl(url);
	// dataSource.setUser(username);
	// dataSource.setPassword(password);
	// if (driverClass != null) {
	// dataSource.setDriverClass(driverClass);
	// }
	// return dataSource;
	// }
	//
	// public static final DataSource dataSource(String url, String username, String
	// password) {
	// return dataSource(url, username, password, null);
	// }
	//
	// public static DataSourceTransactionManager transactionManage(DataSource
	// dataSource) {
	// DataSourceTransactionManager transactionManager = new
	// DataSourceTransactionManager(dataSource);
	// return transactionManager;
	// }
	//
	// /**
	// * 测试链接参数是否有效 有效返回true
	// *
	// * @param url
	// * @param username
	// * @param password
	// * @param driverClass
	// * @return
	// * @throws SQLException
	// */
	// public static final boolean testConnect(String url, String username, String
	// password) throws SQLException {
	// String testSql = "SELECT 1 FROM DUAL";
	// try (Connection connection = DriverManager.getConnection(url, username,
	// password);
	// Statement statement = connection.createStatement();
	// ResultSet resultSet = statement.executeQuery(testSql);) {
	// resultSet.next();
	// return resultSet.getInt(1) == 1;
	// }
	// }
}
