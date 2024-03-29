package com.dber.base.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.dber.base.enums.DberSystem;
import com.dber.base.util.BaseKeyUtil;
import com.dber.base.web.Interceptor.DberSessionControllerInceptor;
import com.dber.cache.ICacheService;
import com.dber.cache.config.CacheConfig;
import com.dber.config.SpringConfig;
import com.dber.config.SystemConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.AbstractView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.dber.base.web.resolver.ExceptionResolver;
import com.dber.base.web.resolver.FastJsonArgumentResolver;

/**
 * <li>文件名称: BaseServiceAppConfig.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月22日
 */
@Configuration
@ComponentScan("com.dber.base.web")
@EnableWebMvc
@EnableRedisHttpSession
@EnableRedisRepositories
@Import({ExceptionResolver.class, FastJsonViewResponseBodyAdvice.class, CacheConfig.class, JCaptchaConfig.class})
@EnableConfigurationProperties({SpringConfig.class})
@EnableAutoConfiguration
public class BaseWebConfig extends WebMvcConfigurerAdapter implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private static final Log log = LogFactory.getLog(BaseWebConfig.class);

    @Autowired
    SystemConfig systemConfig;

    @Autowired
    ICacheService cacheService;

    @Bean
    public DberSystem dberSystem() {
        return DberSystem.valueOf(systemConfig.getService().getName().toUpperCase());
    }

    static {
        JSON.DEFAULT_PARSER_FEATURE |= SerializerFeature.WriteDateUseDateFormat.getMask();
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DberSessionControllerInceptor());
        super.addInterceptors(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new FastJsonArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8000",
                        "http://localhost:8001",
                        "http://localhost:8002",
                        "http://127.0.0.1:8000",
                        "http://127.0.0.1:8001",
                        "http://127.0.0.1:8002")
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders(HttpHeaders.SET_COOKIE)
                .maxAge(1800);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(fastJsonHttpMessageConverter());
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        FastJsonConfig config = converter.getFastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.BrowserSecure,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.PrettyFormat);
//        config.setDateFormat(JSON.DEFFAULT_DATE_FORMAT);
        config.setParserConfig(ParserConfig.global);

        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
//        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
//        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
//        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
//        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
//        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
//        supportedMediaTypes.add(MediaType.APPLICATION_XML);
//        supportedMediaTypes.add(MediaType.IMAGE_GIF);
//        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
//        supportedMediaTypes.add(MediaType.IMAGE_PNG);
//        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
//        supportedMediaTypes.add(MediaType.TEXT_HTML);
//        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
//        supportedMediaTypes.add(MediaType.TEXT_XML);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        return converter;
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("utf-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    public AbstractView jsonView() {
        return new FastJsonJsonView();
    }

    @Bean
    public RequestParamMapMethodArgumentResolver requestParamMapMethodArgumentResolver() {
        return new RequestParamMapMethodArgumentResolver();
    }

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        int port = event.getEmbeddedServletContainer().getPort();
        String ip = null;

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error(e);
        }

        cacheService.put(BaseKeyUtil.getBaseKey(dberSystem()), "http://" + ip + ":" + port);
    }
}