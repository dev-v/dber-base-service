package com.dber.base.web.login;

import com.dber.base.entity.Account;
import com.dber.base.entity.VerifyWay;
import com.dber.base.exception.system.login.*;
import com.dber.base.login.ILoginHelper;
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
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Service
public class DefaultLoginService implements ILoginService {
    private static final String ACCOUNT_KEY = "LOGIN_ACCOUNT";
    private static final Log log = LogFactory.getLog(DefaultLoginService.class);

    @Autowired
    GenericManageableCaptchaService captchaService;

    @Autowired
    private ILoginHelper loginHelper;

    @Override
    public Account getAccount(HttpSession session) throws NotLoginException {
        Object obj = session.getAttribute(ACCOUNT_KEY);
        if (obj == null) {
            throw new NotLoginException();
        } else {
            return (Account) obj;
        }
    }

    @Override
    public Account login(Account account, HttpSession session) {
        VerifyWay verifyWay = account.getVerifyWay();
        if (verifyWay == VerifyWay.account) {
            account = checkByAccountName(account);
        } else if (verifyWay == VerifyWay.cellphone) {

        } else if (verifyWay == VerifyWay.alipay) {

        } else if (verifyWay == VerifyWay.wechat) {

        } else if (verifyWay == VerifyWay.qq) {

        }
        session.setAttribute(ACCOUNT_KEY, account);
        return account;
    }

    @Override
    public boolean logout(HttpSession session) {
        session.invalidate();
        return true;
    }

    @Override
    public boolean regist(Account account, HttpSession session) {
        if (captchaService.validateResponseForID(session.getId(), account.getCaptcha())) {
            Account queryAccount = new Account();
            queryAccount.setName(account.getName());
            if (loginHelper.getAccount(account) != null) {
                throw new AccountExistException();
            }
            loginHelper.saveAccount(account);
            return true;
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
