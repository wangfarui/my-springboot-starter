package com.wfr.springboot.aliyun.service.sls.log.starter;

import com.aliyun.openservices.aliyun.log.producer.Producer;
import com.wfr.springboot.aliyun.service.sls.log.properties.SlsLogProducerProperties;
import com.wfr.springboot.aliyun.service.sls.log.properties.SlsLogProjectProperties;
import com.wfr.springboot.aliyun.service.sls.log.service.SlsLogProducerService;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.interceptor.LogInterceptor;
import com.wfr.springboot.base.log.context.properties.LogContextProperties;
import com.wfr.springboot.base.log.context.starter.LogContextAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * 阿里云SLS日志服务自动装配
 *
 * @author wangfarui
 * @since 2022/7/7
 */
@Configuration
@ConditionalOnClass({LogService.class, Producer.class})
@AutoConfigureAfter({LogContextAutoConfiguration.class})
@ConditionalOnProperty(prefix = "aliyun.sls.log", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({SlsLogProjectProperties.class, SlsLogProducerProperties.class})
public class SlsLogAutoConfiguration implements Ordered {

    public static final int ORDER_PRECEDENCE = Ordered.LOWEST_PRECEDENCE - 1;

    @Bean
    @Primary
    public LogService slsLogProducerService(ObjectProvider<LogContextProperties> logContextProperties,
                                            List<LogInterceptor> logInterceptors,
                                            SlsLogProjectProperties projectProperties,
                                            SlsLogProducerProperties producerProperties) {
        LogContextProperties properties = logContextProperties.getIfAvailable();
        if (properties == null) {
            properties = new LogContextProperties();
        }
        return new SlsLogProducerService(properties, logInterceptors, projectProperties, producerProperties);
    }

    @Override
    public int getOrder() {
        return ORDER_PRECEDENCE;
    }
}
