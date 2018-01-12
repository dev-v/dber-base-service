package com.dber.base.mybatis.plugin.pagination.dialect;

import com.dber.base.mybatis.plugin.pagination.page.Page;
import com.dber.util.Util;

/**
 * <li>文件名称: MysqlDialect.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月20日
 * @author dev-v
 */
public class MysqlDialect extends AbstractDialect {

	@Override
	public String getPageSql(String originalSql, Page<?> page) {
		long offset = (page.getCurrentPage() - 1) * page.getPageSize();
		return originalSql + genenrateOrderBy(page) + " limit " + offset + "," + page.getPageSize();
	}

	private String genenrateOrderBy(Page<?> page) {
		if (!Util.isBlank(page.getSort())) {
			return " order by " + page.getSort();
		}
		return "";
	}
}
