package com.dber.base.exception;

/**
 * <li>文件名称: BusinessException.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: code为：10000 - Integer.MAX_VALUE</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月21日
 * @author dev-v
 */
public class BusinessException extends BaseException {

	private static final long serialVersionUID = -2520876684510502254L;

	private static final int code = 10000;

	static {
		BaseException.registCode(code, BusinessException.class);
	}

	/**
	 * <pre>
	 * 异常代码为10000 - Integer.MAX_VALUE
	 * 其中10000-19999为已占用保留代码
	 * </pre>
	 * 
	 * @param code
	 * @param msg
	 * @param cause
	 */
	public BusinessException(int code, String msg, Throwable cause) {
		super(code, msg, cause);
	}

	public BusinessException() {
		this(code, "服务升级中，请稍候尝试...", null);
	}

}
