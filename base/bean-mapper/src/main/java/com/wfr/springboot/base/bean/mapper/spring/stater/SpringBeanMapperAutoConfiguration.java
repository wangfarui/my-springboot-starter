package com.wfr.springboot.base.bean.mapper.spring.stater;

import com.wfr.springboot.base.bean.mapper.BeanMapper;
import com.wfr.springboot.base.bean.mapper.BeanMapperService;
import com.wfr.springboot.base.bean.mapper.BeanMapperServiceOrdered;
import com.wfr.springboot.base.bean.mapper.spring.SpringBeanMapperService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * {@link BeanMapper} 基于 spring 自动装配
 *
 * @author wangfarui
 * @since 2022/8/12
 */
@Configuration(proxyBeanMethods = false)
public class SpringBeanMapperAutoConfiguration implements Ordered {

    public static final int ORDER_VALUE = BeanMapperServiceOrdered.SPRING.getOrder();

    @Bean("springBeanMapperService")
    @ConditionalOnMissingBean
    public BeanMapperService beanMapperService() {
        return new SpringBeanMapperService();
    }

    @Override
    public int getOrder() {
        return ORDER_VALUE;
    }
}
