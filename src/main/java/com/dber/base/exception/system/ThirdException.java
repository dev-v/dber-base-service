package com.dber.base.exception.system;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.FrameworkException;

/**
 * <li>文件名称: ThirdException.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月21日
 * @author dev-v
 */
public class ThirdException extends FrameworkException {
	private static final long serialVersionUID = -1076070381881587931L;

	private static final int code = 503;

	static {
		BaseException.registCode(code, ThirdException.class);
	}

	public ThirdException(Throwable cause) {
		super(code, "未知信息，请求失联！", cause);
	}

	public ThirdException(String message, Throwable cause) {
		super(code, message, cause);
	}

}
