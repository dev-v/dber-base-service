package com.dber.base.exception;

import java.util.HashMap;
import java.util.Map;

import com.dber.base.exception.system.CodeHasRegistedException;
import com.dber.base.exception.system.IllegalDefineException;

/**
 * <li>文件名称: BaseException.java</li>
 * <li>修改记录: ...</li>
 * 
 * <pre>
 * 通用业务异常定义
 * code代码范围定义：code代码必须为大于或等于300的整形数
 * 300 - 9999 为框架、工具代码、中间件等通用服务预留代码段 开发框架、中间件等通用组件在构建自定义异常架构时，需要预先申请自己的框架异常代码段
 * 10000 - Integer.MAX_VALUE 为业务服务包装异常可用的代码段，每个业务系统在建立业务系统自定义异常构架时，需要预先申请自己的业务异常代码段
 * </pre>
 * 
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月21日
 * @author dev-v
 */
public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 4152857232747212833L;

	private static final Map<Integer, Class<? extends BaseException>> registed_codes = new HashMap<>();

	/**
	 * <pre>
	 * 子类请务必调用该方法注册自己的异常类
	 * </pre>
	 * 
	 * @param code
	 *            自异常类注册代码
	 * @param clz
	 *            自异常类
	 */
	public static final void registCode(Integer code, Class<? extends BaseException> clz) {
		if (registed_codes.containsKey(code)) {
			throw new CodeHasRegistedException(registed_codes.get(code));
		}
		checkDefine(code, clz);
		registed_codes.put(code, clz);
	}

	private int code;

	/**
	 * @param code
	 *            异常代码
	 * @param msg
	 *            异常消息
	 * @param cause
	 *            引起该异常的其他异常
	 * @see BaseException
	 */
	BaseException(int code, String msg, Throwable cause) {
		super(msg, cause);
		this.code = code;
	}

	/**
	 * <pre>
	 * 返回异常代码
	 * </pre>
	 * 
	 * @return
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * <pre>
	 * 检查异常的定义是否合法
	 * </pre>
	 */
	private static final void checkDefine(int code, Class<? extends BaseException> clz) {
		if (FrameworkException.class.isAssignableFrom(clz)) {

			if (code > 299 && code < 9999) {
				return;
			}
		} else if (BusinessException.class.isAssignableFrom(clz)) {
			if (code > 9999) {
				return;
			}
		} else {
			throw new IllegalDefineException();
		}
	}
}
