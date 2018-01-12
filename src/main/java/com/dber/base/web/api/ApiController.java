package com.dber.base.web.api;

import com.dber.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

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
public abstract class ApiController {
    @Autowired
    private SystemConfig config;

}
