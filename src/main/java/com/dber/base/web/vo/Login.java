package com.dber.base.web.vo;

import com.dber.base.entity.Account;
import lombok.Data;

import java.io.Serializable;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/15
 */
@Data
public class Login implements Serializable{
    private Account account;
    private boolean logined;
    private boolean needCaptcha;

    public void setAccount(Account account) {
        logined = true;
        this.account = account;
    }

    public Integer getAccountId() {
        if (account != null) {
            return account.getId();
        } else {
            return null;
        }
    }
}
