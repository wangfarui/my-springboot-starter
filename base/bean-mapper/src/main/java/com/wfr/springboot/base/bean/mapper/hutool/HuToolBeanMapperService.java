package com.wfr.springboot.base.bean.mapper.hutool;

import cn.hutool.core.bean.BeanUtil;
import com.wfr.springboot.base.bean.mapper.AbstractBeanMapperService;

/**
 * 基于 HuTool {@link BeanUtil} 实现的 BeanMapper 服务
 *
 * @author wangfarui
 * @since 2022/8/12
 */
public class HuToolBeanMapperService extends AbstractBeanMapperService {

    @Override
    public <S, D> void copy(S source, D destination) {

    }
}
