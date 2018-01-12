package com.dber.base.exception.system.login;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.FrameworkException;

public class ErrorVerificationCodeException extends FrameworkException {

    private static final int code = 604;

    static {
        BaseException.registCode(code, ErrorVerificationCodeException.class);
    }

    public ErrorVerificationCodeException() {
        super(code, "验证码错误！");
    }
}
