package com.wfr.springboot.base.bean.mapper.orika.converter;

import ma.glasnost.orika.CustomConverter;

/**
 * 自定义转换器的抽象
 *
 * @author wangfarui
 * @since 2022/8/10
 */
public abstract class AbstractCustomConverter<S, D> extends CustomConverter<S, D> {
}
