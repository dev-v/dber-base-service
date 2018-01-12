package com.dber.base.exception.system;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.FrameworkException;

/**
 * <li>文件名称: IllegalDefineException.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月21日
 * @author dev-v
 */
public class IllegalDefineException extends FrameworkException {

	private static final long serialVersionUID = -216160846671997028L;

	private static final int code = 502;

	static {
		BaseException.registCode(code, IllegalDefineException.class);
	}

	public IllegalDefineException() {
		super(code, "异常代码段定义错误或异常类型定义错误，框架异常需要继承FrameworkException，业务异常需要继承！");
	}
}
