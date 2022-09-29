package com.wfr.springboot.base.log.context.starter;

import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.interceptor.BasicInformationFillerLogInterceptor;
import com.wfr.springboot.base.log.context.properties.LogContextProperties;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 日志上下文自动装配
 *
 * @author wangfarui
 * @since 2022/7/12
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Logger.class)
@EnableConfigurationProperties(LogContextProperties.class)
@Import(BasicInformationFillerLogInterceptor.class)
public class LogContextAutoConfiguration {

    static {
        // 初始化时加载LogData
        LogData logData = LogData.emptyLogData();
    }
}
