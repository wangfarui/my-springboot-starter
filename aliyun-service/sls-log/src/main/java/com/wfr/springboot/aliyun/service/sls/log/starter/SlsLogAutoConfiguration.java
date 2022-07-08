package com.wfr.springboot.aliyun.service.sls.log.starter;

import com.aliyun.openservices.aliyun.log.producer.Producer;
import com.aliyun.openservices.log.LogService;
import com.wfr.springboot.aliyun.service.sls.log.properties.SlsLogProducerProperties;
import com.wfr.springboot.aliyun.service.sls.log.properties.SlsLogProjectProperties;
import com.wfr.springboot.aliyun.service.sls.log.service.DefaultSlsLogProducerService;
import com.wfr.springboot.aliyun.service.sls.log.service.SlsLogProducerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云SLS日志服务自动装配
 *
 * @author wangfarui
 * @since 2022/7/7
 */
@Configuration
@ConditionalOnClass({LogService.class, Producer.class})
@ConditionalOnProperty(prefix = "sls.log", name = "enabled", havingValue = "true")
@EnableConfigurationProperties({SlsLogProjectProperties.class, SlsLogProducerProperties.class})
public class SlsLogAutoConfiguration {

    @Bean
    public SlsLogProducerService slsLogProducerService(SlsLogProjectProperties projectProperties,
                                                       SlsLogProducerProperties producerProperties) {
        return new DefaultSlsLogProducerService(projectProperties, producerProperties);
    }
}
