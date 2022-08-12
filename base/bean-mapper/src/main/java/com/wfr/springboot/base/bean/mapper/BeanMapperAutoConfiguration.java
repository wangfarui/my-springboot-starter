package com.wfr.springboot.base.bean.mapper;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * {@link BeanMapper} 自动装配
 *
 * @author wangfarui
 * @since 2022/8/12
 */
@Configuration(proxyBeanMethods = false)
public class BeanMapperAutoConfiguration {

    @PostConstruct
    public void initBeanMapper(BeanMapperService beanMapperService) {
        BeanMapper.setBeanMapperService(beanMapperService);
    }

}
