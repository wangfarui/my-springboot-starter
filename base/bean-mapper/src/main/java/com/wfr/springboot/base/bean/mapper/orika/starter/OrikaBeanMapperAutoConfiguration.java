package com.wfr.springboot.base.bean.mapper.orika.starter;

import com.wfr.springboot.base.bean.mapper.BeanMapper;
import com.wfr.springboot.base.bean.mapper.BeanMapperServiceOrdered;
import com.wfr.springboot.base.bean.mapper.orika.converter.AbstractCustomConverter;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;

/**
 * {@link BeanMapper} 基于 orika 自动装配
 *
 * @author wangfarui
 * @since 2022/8/3
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = AbstractCustomConverter.class)
public class OrikaBeanMapperAutoConfiguration implements Ordered {

    /**
     * {@link Converter} Bean的注册尽量在 {@link OrikaBeanMapperAutoConfiguration} 之前
     */
    public static final int ORDER_VALUE = BeanMapperServiceOrdered.ORIKA.getOrder();

    @Bean
    @ConditionalOnMissingBean
    public DefaultMapperFactory mapperFactoryBuilder(ObjectProvider<Converter<?, ?>> converters) {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        converters.orderedStream().forEach(converterFactory::registerConverter);
        return mapperFactory;
    }

    @Primary
    @Bean("defaultMapperFacade")
    public MapperFacade mapperFacade(MapperFactory mapperFactory) {
        return mapperFactory.getMapperFacade();
    }

    @Override
    public int getOrder() {
        return ORDER_VALUE;
    }
}
