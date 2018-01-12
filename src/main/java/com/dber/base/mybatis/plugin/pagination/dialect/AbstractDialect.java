package com.dber.base.mybatis.plugin.pagination.dialect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dber.base.mybatis.plugin.pagination.page.Page;

/**
 * <li>文件名称: AbstractDialect.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月20日
 * @author dev-v
 */
public abstract class AbstractDialect {
	private static final Pattern FROM_PATTERN = Pattern.compile("[\\s]from[\\s]+.+",
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	private static final String COUNT_STRING = "select count(*) ";

	private static final Pattern GROUP_BY = Pattern.compile("\\sgroup\\sby\\s[^)]+$", Pattern.CASE_INSENSITIVE);

	public abstract String getPageSql(String originalSql, Page<?> page);

	public static String getCountSql(String originalSql) {
		final Matcher m = GROUP_BY.matcher(originalSql);
		if (m.find()) {
			return "select count(1) from (" + originalSql + ") page";
		}
		final Matcher matcher = FROM_PATTERN.matcher(originalSql);
		matcher.find();
		return COUNT_STRING + matcher.group();
	}
}
