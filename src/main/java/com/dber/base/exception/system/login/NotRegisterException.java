package com.dber.base.exception.system.login;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.FrameworkException;

public class NotRegisterException extends FrameworkException {

    private static final int code = 602;

    static {
        BaseException.registCode(code, NotRegisterException.class);
    }

    public NotRegisterException() {
        super(code, "未找到用户，请检查用户名是否正确，或请先注册！");
    }
}
