package com.wfr.springboot.aliyun.service.sls.log;

import com.wfr.springboot.aliyun.service.sls.log.service.SlsLogProducerService;
import com.wfr.springboot.base.environment.BaseEnvironment;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;

/**
 * 阿里云SLS日志 Logger
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public abstract class Logger {

    private volatile SlsLogProducerService logProducerService;

    private SlsLogProducerService logProducerService() {
        if (this.logProducerService == null) {
            synchronized (Logger.class) {
                if (this.logProducerService == null) {
                    ObjectProvider<SlsLogProducerService> beanProvider = BaseEnvironment
                            .applicationContext()
                            .getBeanProvider(SlsLogProducerService.class);
                    SlsLogProducerService service = beanProvider.getIfAvailable();
                    if (service == null) {
                        throw new NoSuchBeanDefinitionException("there is no SlsLogProducerService.class bean");
                    }
                    logProducerService = service;
                }
            }
        }
        return this.logProducerService;
    }

}
