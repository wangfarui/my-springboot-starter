package com.wfr.springboot.base.web.context.starter;

import com.wfr.springboot.base.environment.BaseEnvironment;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.service.AbstractLogService;
import com.wfr.springboot.base.web.context.advice.log.LogWebBodyAdvice;
import com.wfr.springboot.base.web.context.properties.WebRequestLogProperties;
import com.wfr.springboot.base.web.context.service.WebRequestLogService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
@ComponentScan(basePackageClasses = LogWebBodyAdvice.class)
public class WebRequestLogAutoConfiguration {

    public static final String WEB_REQUEST_LOG_PROPERTIES_PREFIX = "log.web";

    public static final String REQUEST_LOG_SERVICE_BEAN_NAME = "requestLogService";

    @Bean(name = REQUEST_LOG_SERVICE_BEAN_NAME)
    @ConditionalOnMissingBean(name = REQUEST_LOG_SERVICE_BEAN_NAME)
    @ConditionalOnBean(AbstractLogService.class)
    public LogService requestLogService(WebRequestLogProperties logProperties) {
        ConfigurableApplicationContext applicationContext = BaseEnvironment.applicationContext();
        ObjectProvider<AbstractLogService> beanProvider = applicationContext.getBeanProvider(AbstractLogService.class);
        AbstractLogService primaryLogService = beanProvider.getIfUnique();
        if (primaryLogService == null) {
            primaryLogService = applicationContext.getBean(AbstractLogService.DEFAULT_LOG_SERVICE, AbstractLogService.class);
        }
        return new WebRequestLogService(primaryLogService, logProperties);
    }

}
