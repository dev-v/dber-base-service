package com.dber.base.exception.system.login;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.FrameworkException;

public class AccountExistException extends FrameworkException {

    private static final int code = 605;

    static {
        BaseException.registCode(code, AccountExistException.class);
    }

    public AccountExistException() {
        super(code, "用户名已存在！");
    }
}
