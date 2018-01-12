package com.dber.base.mybatis.plugin.pagination.ext;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <li>文件名称: PageParameter.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月21日
 * @author dev-v
 */
@Data
@AllArgsConstructor
public class PageParameter {
	private String name;
	private Object value;
	private JdbcType jdbcType;
	@SuppressWarnings("rawtypes")
	private TypeHandler typeHandler;
}
