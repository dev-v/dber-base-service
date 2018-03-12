package com.dber.base.web.resolver;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.util.TypeUtils;
import com.dber.util.Util;

/**
 * <li>文件名称: FastJsonArgumentResolver.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月22日
 */
public class FastJsonArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return !Util.isPremitive(parameter.getParameterType());
  }

  /**
   * 解析 pojo map array
   */
  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    Map<String, String[]> map = request.getParameterMap();
    Map<String, Object> parameters = new HashMap<>();
    String[] val;
    String v;
    for (String key : map.keySet()) {
      val = map.get(key);
      v = val[0].trim();
      if (val.length == 1 && !v.isEmpty()) {
        parameters.put(key, v);
      } else {
        parameters.put(key, val);
      }
    }
    return TypeUtils.castToJavaBean(parameters, parameter.getParameterType());
  }
}
