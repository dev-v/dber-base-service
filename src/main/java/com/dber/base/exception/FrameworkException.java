package com.dber.base.exception;

/**
 * <li>文件名称: FrameworkException.java</li>
 * <li>修改记录: ...</li>
 * 
 * <pre>
 * 框架本身的异常
 * 异常代码为300-9999
 * </pre>
 * 
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月21日
 * @author dev-v
 */
public class FrameworkException extends BaseException {

	private static final long serialVersionUID = -1049649141716201263L;

	private static final int code = 500;

	static {
		BaseException.registCode(code, FrameworkException.class);
	}

	/**
	 * <pre>
	 * 异常代码为500-9999
	 * 其中500-5999为已占用保留异常代码
	 * </pre>
	 * 
	 * @param code
	 * @param msg
	 * @param cause
	 */
	public FrameworkException(int code, String msg, Throwable cause) {
		super(code, msg, cause);
	}

	/**
	 * <pre>
	 * 异常代码为500-9999
	 * 其中500-5999为已占用保留异常代码
	 * </pre>
	 * 
	 * @param code
	 * @param msg
	 */
	public FrameworkException(int code, String msg) {
		this(code, msg, null);
	}

	public FrameworkException() {
		super(code, "系统维护中，请稍后尝试...", null);
	}
}
