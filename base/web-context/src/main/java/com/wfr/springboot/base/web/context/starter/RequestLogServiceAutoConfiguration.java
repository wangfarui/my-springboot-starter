package com.wfr.springboot.base.web.context.starter;

import com.wfr.springboot.base.environment.BaseEnvironment;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.service.AbstractLogService;
import com.wfr.springboot.base.log.context.starter.LogServiceAutoConfiguration;
import com.wfr.springboot.base.web.context.http.HttpRequestTraceLogFilter;
import com.wfr.springboot.base.web.context.properties.WebRequestLogProperties;
import com.wfr.springboot.base.web.context.service.WebRequestLogService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 * spring web请求日志自动装配
 *
 * @author wangfarui
 * @since 2022/7/15
 */
@Configuration
@ConditionalOnClass(AbstractLogService.class)
@ConditionalOnProperty(prefix = RequestLogServiceAutoConfiguration.WEB_REQUEST_LOG_PROPERTIES_PREFIX,
        name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter({LogServiceAutoConfiguration.class})
public class RequestLogServiceAutoConfiguration {

    public static final String WEB_REQUEST_LOG_PROPERTIES_PREFIX = "web.log";

    public static final String WEB_REQUEST_LOG_SERVICE_BEAN_NAME = "webRequestLogService";

    @Bean(name = WEB_REQUEST_LOG_SERVICE_BEAN_NAME)
    @ConditionalOnMissingBean(name = WEB_REQUEST_LOG_SERVICE_BEAN_NAME)
    @ConditionalOnBean(value = AbstractLogService.class)
    public LogService webRequestLogService() {
        ConfigurableApplicationContext applicationContext = BaseEnvironment.applicationContext();
        ObjectProvider<AbstractLogService> beanProvider = applicationContext.getBeanProvider(AbstractLogService.class);
        AbstractLogService primaryLogService = beanProvider.getIfUnique();
        if (primaryLogService == null) {
            primaryLogService = applicationContext.getBean(AbstractLogService.DEFAULT_LOG_SERVICE, AbstractLogService.class);
        }
        return new WebRequestLogService(primaryLogService);
    }

    /**
     * web请求链路日志的自动装配
     *
     * <p>请求链路日志类型</p>
     * <ul>
     *     <li>http</li>
     *     <li>feign</li>
     *     <li>http utils</li>
     * </ul>
     */
    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(WebRequestLogProperties.class)
    static class RequestTraceLogAutoConfiguration {

        /**
         * http请求链路日志自动装配
         */
        @Bean
        @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
        public HttpRequestTraceLogFilter httpRequestTraceLogFilter(
                @Qualifier(WEB_REQUEST_LOG_SERVICE_BEAN_NAME) @Nullable LogService logService,
                WebRequestLogProperties logProperties) {
            return new HttpRequestTraceLogFilter(logService, logProperties);
        }
    }

}
