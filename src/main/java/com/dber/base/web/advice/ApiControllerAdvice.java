package com.dber.base.web.advice;

import com.dber.base.IClient;
import com.dber.base.util.BaseKeyUtil;
import com.dber.config.SystemConfig;
import com.dber.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/12
 */
@EnableConfigurationProperties({SystemConfig.class})
@ControllerAdvice(assignableTypes = IClient.class)
@Configuration
public class ApiControllerAdvice {

    @Autowired
    private SystemConfig config;

    private Map<String, String> grant;

    @ModelAttribute
    public void validAppAndKey(@RequestParam(BaseKeyUtil.auth_params_system) String app
            , @RequestParam(BaseKeyUtil.auth_params_key) String key) {
        if (Util.isBlank(app)) {
            throw new IllegalArgumentException("valid-error！");
        }
        String grantKey = grant.get(app.toLowerCase());
        if (grantKey == null) {
            throw new IllegalArgumentException("valid-error！");
        }
        if (!grantKey.equals(key)) {
            throw new IllegalArgumentException("valid-error！");
        }
    }

    @PostConstruct
    public void init() {
        grant = config.getAuth().getGrant();
    }
}
