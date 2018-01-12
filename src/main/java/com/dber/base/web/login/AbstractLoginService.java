package com.dber.base.web.login;

import com.dber.base.exception.system.login.*;
import com.dber.util.CipherUtil;
import com.dber.util.Util;
import com.dber.base.web.vo.Account;
import com.dber.base.web.vo.VerifyWay;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public abstract class AbstractLoginService implements ILoginService {
    private static final String ACCOUNT_KEY = "LOGIN_ACCOUNT";
    private static final Log log = LogFactory.getLog(AbstractLoginService.class);

    @Autowired
    GenericManageableCaptchaService captchaService;

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
        if (account.getVerifyWay() == VerifyWay.account) {
            account = checkByAccountName(account);
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

    @Override
    public boolean validVerificationCode(String code, HttpSession session) {
        return captchaService.validateResponseForID(session.getId(), code).booleanValue();
    }

    private Account checkByAccountName(Account account) {
        String name = account.getName();
        String pass = account.getPassword();
        if (Util.isBlank(name) || Util.isBlank(pass)) {
            throw new EmptyNameOrPasswordException();
        }
        Account dbAccount = getAccountByName(name);
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

    /**
     * 根据用户名获取账户信息
     * 若没有该用户返回null
     *
     * @param name
     * @return
     */
    abstract protected Account getAccountByName(String name);

    /**
     * 新建Account账户
     *
     * @param account
     */
    abstract protected void newAccount(Account account);
}
