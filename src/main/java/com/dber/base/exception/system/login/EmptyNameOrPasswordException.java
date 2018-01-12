package com.dber.base.exception.system.login;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.FrameworkException;

public class EmptyNameOrPasswordException extends FrameworkException {

    private static final int code = 601;

    static {
        BaseException.registCode(code, EmptyNameOrPasswordException.class);
    }

    public EmptyNameOrPasswordException() {
        super(code, "用户名或密码不能为空！");
    }
}
