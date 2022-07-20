package com.wfr.springboot.base.web.context.starter;

import com.wfr.springboot.base.environment.BaseEnvironment;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.service.AbstractLogService;
import com.wfr.springboot.base.web.context.advice.WebRequestLogResponseBodyAdvice;
import com.wfr.springboot.base.web.context.config.WebRequestLogWebMvcConfigurer;
import com.wfr.springboot.base.web.context.properties.WebRequestLogProperties;
import com.wfr.springboot.base.web.context.service.WebRequestLogService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * spring web请求日志自动装配
 *
 * @author wangfarui
 * @since 2022/7/15
 */
@Configuration
@ConditionalOnClass(AbstractLogService.class)
@ConditionalOnProperty(prefix = WebRequestLogAutoConfiguration.WEB_REQUEST_LOG_PROPERTIES_PREFIX,
        name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(WebRequestLogProperties.class)
@ComponentScan(basePackageClasses = {WebRequestLogResponseBodyAdvice.class})
public class WebRequestLogAutoConfiguration {

    public static final String WEB_REQUEST_LOG_PROPERTIES_PREFIX = "web.log";

    public static final String WEB_REQUEST_LOG_SERVICE_BEAN_NAME = "webRequestLogService";

    private final WebRequestLogProperties logProperties;

    @Autowired
    public WebRequestLogAutoConfiguration(WebRequestLogProperties logProperties) {
        this.logProperties = logProperties;
    }

    @Bean(name = WEB_REQUEST_LOG_SERVICE_BEAN_NAME)
    @ConditionalOnMissingBean(name = WEB_REQUEST_LOG_SERVICE_BEAN_NAME)
    @ConditionalOnBean(AbstractLogService.class)
    public LogService webRequestLogService() {
        ConfigurableApplicationContext applicationContext = BaseEnvironment.applicationContext();
        ObjectProvider<AbstractLogService> beanProvider = applicationContext.getBeanProvider(AbstractLogService.class);
        AbstractLogService primaryLogService = beanProvider.getIfUnique();
        if (primaryLogService == null) {
            primaryLogService = applicationContext.getBean(AbstractLogService.DEFAULT_LOG_SERVICE, AbstractLogService.class);
        }
        return new WebRequestLogService(primaryLogService, this.logProperties);
    }

    @Bean
    @ConditionalOnClass(WebMvcConfigurationSupport.class)
    public WebMvcConfigurer webRequestLogWebMvcConfigurer() {
        return new WebRequestLogWebMvcConfigurer(this.logProperties.getFilter());
    }

}
