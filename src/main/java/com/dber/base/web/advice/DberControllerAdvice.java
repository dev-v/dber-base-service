package com.dber.base.web.advice;

import com.dber.base.web.controller.AbstractReadController;
import com.dber.base.web.login.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/15
 */
@ControllerAdvice(assignableTypes = {AbstractReadController.class})
@Component
public class DberControllerAdvice {

    @Autowired
    private ILoginService loginService;

    @ModelAttribute
    public void checkLogin() {
        loginService.getAccount();
    }
}
