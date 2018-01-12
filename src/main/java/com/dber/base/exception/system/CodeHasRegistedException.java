package com.dber.base.exception.system;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.FrameworkException;

/**
 * <li>文件名称: CodeHasRegistedException.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月21日
 * @author dev-v
 */
public class CodeHasRegistedException extends FrameworkException {

	private static final long serialVersionUID = 1084837134069657710L;

	private static final int code = 501;

	static {
		BaseException.registCode(code, CodeHasRegistedException.class);
	}

	public CodeHasRegistedException(Class<? extends BaseException> clz) {
		super(code, "异常代码已注册,请重新选择代码:" + clz);
	}

}
