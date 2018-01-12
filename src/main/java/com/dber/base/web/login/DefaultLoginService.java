package com.dber.base.web.login;

import com.dber.util.CipherUtil;
import com.dber.base.web.vo.Account;
import org.springframework.stereotype.Service;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/10
 */
@Service
public class DefaultLoginService extends AbstractLoginService {
    @Override
    protected Account getAccountByName(String name) {
        Account account = new Account();
        account.setId(1);
        account.setName(name);
        account.setCellphone("13111111111");
        account.setPassword(CipherUtil.SHAEncode("888888"));
        return account;
    }

    @Override
    protected void newAccount(Account account) {

    }
}
