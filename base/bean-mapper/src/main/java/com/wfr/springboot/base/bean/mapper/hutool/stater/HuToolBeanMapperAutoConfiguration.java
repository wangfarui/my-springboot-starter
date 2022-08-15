package com.wfr.springboot.base.bean.mapper.hutool.stater;

import cn.hutool.core.bean.BeanUtil;
import com.wfr.springboot.base.bean.mapper.BeanMapper;
import com.wfr.springboot.base.bean.mapper.BeanMapperService;
import com.wfr.springboot.base.bean.mapper.BeanMapperServiceOrdered;
import com.wfr.springboot.base.bean.mapper.hutool.HuToolBeanMapperService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * {@link BeanMapper} 基于 hutool 自动装配
 *
 * @author wangfarui
 * @since 2022/8/12
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(BeanUtil.class)
public class HuToolBeanMapperAutoConfiguration implements Ordered {

    public static final int ORDER_VALUE = BeanMapperServiceOrdered.HU_TOOL.getOrder();

    @Bean("huToolBeanMapperService")
    @ConditionalOnMissingBean
    public BeanMapperService beanMapperService() {
        return new HuToolBeanMapperService();
    }

    @Override
    public int getOrder() {
        return ORDER_VALUE;
    }
}
