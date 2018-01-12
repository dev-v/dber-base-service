package com.dber.base.web.advice;

import com.dber.base.util.BaseKeyUtil;
import com.dber.config.SystemConfig;
import com.dber.util.Util;
import org.aopalliance.reflect.Method;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
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
@ControllerAdvice(basePackages = "com.dber.*.web.api")
@Configuration
@EnableAspectJAutoProxy
public class ApiControllerAdvice implements MethodBeforeAdvice{

    @Autowired
    private SystemConfig config;

    private Map<String, String> grant;

    @ModelAttribute
    public void validAppAndKey(HttpServletRequest request) {
        String app = request.getParameter(BaseKeyUtil.auth_params_system);
        if (Util.isBlank(app)) {
            throw new IllegalArgumentException("未知错误！");
        }
        String key = grant.get(app);
        if (key == null) {
            throw new IllegalArgumentException("未知错误！");
        }
        String requestKey = request.getParameter(BaseKeyUtil.auth_params_key);
        if (!key.equals(requestKey)) {
            throw new IllegalArgumentException("未知错误！");
        }
    }

    @PostConstruct
    public void init() {
        grant = config.getAuth().getGrant();
    }

    @Override
    public void before(java.lang.reflect.Method method, Object[] args, Object target) throws Throwable {
        System.out.println(args);
    }
}
