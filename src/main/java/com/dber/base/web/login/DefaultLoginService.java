package com.dber.base.web.login;

import com.dber.base.entity.Account;
import com.dber.base.entity.VerifyWay;
import com.dber.base.exception.system.login.*;
import com.dber.base.login.ILoginHelper;
import com.dber.base.result.Result;
import com.dber.base.web.Interceptor.DberSessionControllerInceptor;
import com.dber.base.web.vo.Login;
import com.dber.util.CipherUtil;
import com.dber.util.Util;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Service
public class DefaultLoginService implements ILoginService {
    private String ACCOUNT_KEY = "LOGIN_ACCOUNT";
    private static final Log log = LogFactory.getLog(DefaultLoginService.class);

    @Autowired
    GenericManageableCaptchaService captchaService;

    @Autowired
    private ILoginHelper loginHelper;

    @Override
    public Login getLogin() throws NotLoginException {
        Login login = get(ACCOUNT_KEY);
        if (login == null) {
            login = new Login();
        }
        return login;
    }

    @Override
    public Account getAccount() throws NotLoginException {
        Login login = getLogin();
        if (login.getAccount() == null) {
            throw new NotLoginException();
        }
        return login.getAccount();
    }

    @Override
    public Integer getAccountId() throws NotLoginException {
        return getAccount().getId();
    }

    @Override
    public Login login(Account account) {
        Login login = getLogin();
        if (login.isNeedCaptcha()) {
            if (!captchaService.validateResponseForID(DberSessionControllerInceptor.getSessionId(), account.getCaptcha()).booleanValue()) {
                throw new ErrorVerificationCodeException();
            }
        }
        try {
            VerifyWay verifyWay = account.getVerifyWay();
            if (verifyWay == VerifyWay.account) {
                account = checkByAccountName(account);
            } else if (verifyWay == VerifyWay.cellphone) {

            } else if (verifyWay == VerifyWay.alipay) {

            } else if (verifyWay == VerifyWay.wechat) {

            } else if (verifyWay == VerifyWay.qq) {

            } else {
                throw new IllegalArgumentException("请设置登录方式！");
            }
            login.setAccount(account);
        } catch (Exception e) {
            login.setNeedCaptcha(true);
            storeLogin(login);
            throw e;
        }
        storeLogin(login);
        return login;
    }

    private void storeLogin(Login login) {
        store(ACCOUNT_KEY, login);
    }

    @Override
    public boolean logout() {
        Login login = getLogin();
        // 这里必须校验是为登录状态 否则存在风险
        if (login != null && login.isLogined()) {
            DberSessionControllerInceptor.remove(ACCOUNT_KEY);
        }
        return true;
    }

    @Override
    public boolean regist(Account account) {
        if (captchaService.validateResponseForID(DberSessionControllerInceptor.getSessionId(), account.getCaptcha())) {
            Account queryAccount = new Account();
            queryAccount.setName(account.getName());
            if (loginHelper.getAccount(account) != null) {
                throw new AccountExistException();
            }
            account.setPassword(CipherUtil.SHAEncode(account.getPassword()));
            Result<Account> result = loginHelper.saveAccount(account);
            return result.isSuccess();
        } else {
            throw new ErrorVerificationCodeException();
        }
    }

    @Override
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        try {
            String captchaId = request.getSession().getId();
            BufferedImage challenge = captchaService.getImageChallengeForID(captchaId);
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
            jpegEncoder.encode(challenge);
            byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            try (ServletOutputStream out = response.getOutputStream()) {
                out.write(captchaChallengeAsJpeg);
                out.flush();
            } catch (Exception e) {
                log.error(e);
            }
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e1) {
                log.error(e1);
            }
            log.error(e);
        }
    }

    @Override
    public void store(String key, Object val) {
        DberSessionControllerInceptor.store(key, val);
    }

    @Override
    public <E> E get(String key) {
        return DberSessionControllerInceptor.get(key);
    }

    private Account checkByAccountName(Account account) {
        String name = account.getName();
        String pass = account.getPassword();
        account.setPassword(null);
        if (Util.isBlank(name) || Util.isBlank(pass)) {
            throw new EmptyNameOrPasswordException();
        }
        Account dbAccount = loginHelper.getAccount(account);
        if (dbAccount == null) {
            throw new NotRegisterException();
        }
        if (CipherUtil.SHAEncode(pass).equals(dbAccount.getPassword())) {
            dbAccount.setPassword(null);
            return dbAccount;
        } else {
            throw new ErrorPasswordException();
        }
    }
}
