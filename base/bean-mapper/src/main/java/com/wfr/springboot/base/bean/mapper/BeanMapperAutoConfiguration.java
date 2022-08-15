package com.wfr.springboot.base.bean.mapper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;

/**
 * {@link BeanMapper} 自动装配
 *
 * @author wangfarui
 * @since 2022/8/12
 */
@Configuration(proxyBeanMethods = false)
public class BeanMapperAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @PostConstruct
    public void initBeanMapper() {
        ObjectProvider<BeanMapperService> beanProvider = applicationContext.getBeanProvider(BeanMapperService.class);
        BeanMapperService beanMapperService = beanProvider.getIfUnique();
        if (beanMapperService == null) {
            throw new IllegalArgumentException("没有唯一的 BeanMapperService Bean, 请检查配置");
        }
        BeanMapper.setBeanMapperService(beanMapperService);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
