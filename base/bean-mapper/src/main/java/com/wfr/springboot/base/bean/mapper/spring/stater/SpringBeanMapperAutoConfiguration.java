package com.wfr.springboot.base.bean.mapper.spring.stater;

import com.wfr.springboot.base.bean.mapper.BeanMapper;
import org.springframework.context.annotation.Configuration;

/**
 * {@link BeanMapper} 基于 spring 自动装配
 *
 * @author wangfarui
 * @since 2022/8/12
 */
@Configuration(proxyBeanMethods = false)
public class SpringBeanMapperAutoConfiguration {
}
