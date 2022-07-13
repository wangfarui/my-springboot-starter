package com.wfr.springboot.base.log.context.starter;

import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.interceptor.LogInterceptor;
import com.wfr.springboot.base.log.context.properties.LogContextProperties;
import com.wfr.springboot.base.log.context.service.GenericLogService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * 日志拦截器自动装配
 *
 * @author wangfarui
 * @see LogInterceptor
 * @since 2022/7/12
 */
@Configuration
@AutoConfigureAfter(LogContextAutoConfiguration.class)
public class LogServiceAutoConfiguration implements Ordered {

    public static final int ORDER_VALUE = Ordered.LOWEST_PRECEDENCE;

    @Bean
    @ConditionalOnMissingBean
    public LogService defaultLogService(ObjectProvider<LogContextProperties> logContextProperties, List<LogInterceptor> logInterceptors) {
        LogContextProperties properties = logContextProperties.getIfAvailable();
        if (properties == null) {
            properties = new LogContextProperties();
        }
        return new GenericLogService(properties, logInterceptors);
    }

    @Override
    public int getOrder() {
        return ORDER_VALUE;
    }
}
