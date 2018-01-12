package com.dber.base.web.login;

import com.dber.base.exception.system.login.NotLoginException;
import com.dber.base.web.vo.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <li>文件名称: ILoginCheckService.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: 登录管理</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月21日
 */
public interface ILoginService {

    /**
     * <pre>
     * 获取已登录用户的账号信息
     * </pre>
     *
     * @return 账号
     * 没登录或登录过期 且force为true时抛出异常
     */
    Account getAccount(HttpSession session) throws NotLoginException;

    /**
     * 进行登录操作
     * 未注册、密码错误、密码错误次数限制等
     *
     * @param account
     * @return 返回登录成功的账户信息
     */
    Account login(Account account, HttpSession session);

    boolean logout(HttpSession session);

    /**
     * 注册账号
     *
     * @param account
     * @param session
     * @return 注册成功返回true
     */
    boolean regist(Account account, HttpSession session);

    /**
     * 获取验证码(图形验证码或手机验证码)
     *
     * @param
     */
    void getCaptcha(HttpServletRequest request, HttpServletResponse response);

    boolean validVerificationCode(String code, HttpSession session);
}
