package com.dber.base.exception.system.login;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.BusinessException;

public class ErrorVerificationCodeException extends BusinessException {

    private static final int code = 10001;

    static {
        BaseException.registCode(code, ErrorVerificationCodeException.class);
    }

    public ErrorVerificationCodeException() {
        super(code, "验证码错误！");
    }
}
