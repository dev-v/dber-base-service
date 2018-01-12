package com.dber.base.exception.system;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.FrameworkException;

/**
 * <li>文件名称: NotFoundException.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月22日
 * @author dev-v
 */
public class NotFoundException extends FrameworkException {

	private static final long serialVersionUID = 8927782797176188682L;

	private static final int code = 404;

	static {
		BaseException.registCode(code, NotFoundException.class);
	}

	/**
	 * 一般用于根据ID查询数据 或者数据返回null的情况
	 */
	public NotFoundException() {
		super(code, "没有找到相关信息！");
	}

}
