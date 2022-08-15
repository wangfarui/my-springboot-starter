package com.wfr.springboot.base.bean.mapper.spring;

import com.wfr.springboot.base.bean.mapper.AbstractBeanMapperService;
import org.springframework.beans.BeanUtils;

/**
 * 基于 Spring {@link BeanUtils} 实现的 BeanMapper 服务
 *
 * @author wangfarui
 * @since 2022/8/12
 */
public class SpringBeanMapperService extends AbstractBeanMapperService {

    @Override
    public <S, D> void copy(S source, D destination) {
        BeanUtils.copyProperties(source, destination);
    }
}
