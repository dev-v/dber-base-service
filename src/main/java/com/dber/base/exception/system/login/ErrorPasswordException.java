package com.dber.base.exception.system.login;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.BusinessException;
import com.dber.base.exception.FrameworkException;

public class ErrorPasswordException extends BusinessException {

    private static final int code = 10003;

    static {
        BaseException.registCode(code, ErrorPasswordException.class);
    }

    public ErrorPasswordException() {
        super(code, "密码错误！");
    }
}
