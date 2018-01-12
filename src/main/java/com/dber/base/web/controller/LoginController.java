package com.dber.base.web.controller;

import com.dber.base.exception.system.login.NotLoginException;
import com.dber.base.web.login.ILoginService;
import com.dber.base.web.vo.Account;
import com.dber.base.web.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/10
 */
@RestController
@RequestMapping("/login/")
public class LoginController {

    @Autowired
    ILoginService loginService;

    @RequestMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        loginService.getCaptcha(request, response);
    }

    @RequestMapping("login")
    public Response<Account> login(Account account, HttpSession session) {
        account = loginService.login(account, session);
        return Response.newSuccessResponse(account);
    }

    @RequestMapping("getAccount")
    public Response<Account> getAccount(HttpSession session) {
        Account account = null;
        try {
            account = loginService.getAccount(session);
        } catch (NotLoginException e) {

        }
        return Response.newSuccessResponse(account);
    }

    @RequestMapping("logout")
    public Response<Boolean> logout(HttpSession session) {
        return Response.newSuccessResponse(loginService.logout(session));
    }

    @RequestMapping("regist")
    public Response regist(Account account, HttpSession session) {
        return Response.newSuccessResponse(loginService.regist(account, session));
    }
}