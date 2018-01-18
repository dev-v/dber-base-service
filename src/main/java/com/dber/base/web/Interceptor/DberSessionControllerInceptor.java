package com.dber.base.web.Interceptor;

import com.dber.base.enums.DberSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/15
 */
@Component
public class DberSessionControllerInceptor extends HandlerInterceptorAdapter {
    private static String SYSTEM_KEY;

    @Autowired
    private DberSystem dberSystem;

    private static final ThreadLocal<HttpSession> sessions = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        sessions.set(request.getSession());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        sessions.remove();
        super.afterCompletion(request, response, handler, ex);
    }

//    public static final HttpSession getSession() {
//        return sessions.get();
//    }

    public static final String getSessionId() {
        return sessions.get().getId();
    }

    public static final void store(String key, Object val) {
        sessions.get().setAttribute(SYSTEM_KEY + key, val);
    }

    public static final <E> E get(String key) {
        return (E) sessions.get().getAttribute(SYSTEM_KEY + key);
    }

    public static final void remove(String key) {
        sessions.get().removeAttribute(SYSTEM_KEY + key);
    }

    @PostConstruct
    private void init() {
        SYSTEM_KEY = this.dberSystem.name() + "_DBER_SESSION_";
    }
}
