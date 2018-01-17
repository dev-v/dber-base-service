package com.dber.base.web.controller;

import com.dber.base.entity.Account;
import com.dber.base.exception.system.login.NotLoginException;
import com.dber.base.web.login.ILoginService;
import com.dber.base.web.vo.Login;
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
    public Response<Login> login(Account account) {
        return Response.newSuccessResponse(loginService.login(account));
    }

    @RequestMapping("getLogin")
    public Response<Login> getLogin() {
        return Response.newSuccessResponse(loginService.getLogin());
    }

    @RequestMapping("logout")
    public Response<Boolean> logout() {
        return Response.newSuccessResponse(loginService.logout());
    }

    @RequestMapping("regist")
    public Response regist(Account account) {
        return Response.newSuccessResponse(loginService.regist(account));
    }
}