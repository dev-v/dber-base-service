package com.dber.base.web.login;

import com.dber.base.entity.Account;
import com.dber.base.exception.system.login.NotLoginException;
import com.dber.base.web.vo.Login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     * 获取登录用户的账号信息
     * </pre>
     *
     * @return 账号
     */
    Login getLogin();

    /**
     * @return
     * @throws NotLoginException
     */
    Account getAccount() throws NotLoginException;

    /**
     * @return
     * @throws NotLoginException
     */
    Integer getAccountId() throws NotLoginException;

    /**
     * 进行登录操作
     * 未注册、密码错误、密码错误次数限制等
     *
     * @param account
     * @return 返回登录成功的账户信息
     */
    Login login(Account account);

    boolean logout();

    /**
     * 根据账号密码注册账号
     *
     * @param account
     * @return 注册成功返回true
     */
    boolean regist(Account account);

    /**
     * 获取验证码(图形验证码或手机验证码)
     *
     * @param
     */
    void getCaptcha(HttpServletRequest request, HttpServletResponse response);

    /**
     * 往session存储数据
     *
     * @param key
     * @param val
     */
    void store(String key, Object val);

    /**
     * 获取数据
     *
     * @param key
     */
    <E> E get(String key);
}
