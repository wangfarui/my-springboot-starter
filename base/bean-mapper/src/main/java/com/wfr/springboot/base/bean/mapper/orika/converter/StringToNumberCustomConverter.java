package com.wfr.springboot.base.bean.mapper.orika.converter;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

/**
 * {@link String} 转 {@link Number} 类型的转换器
 *
 * @author wangfarui
 * @since 2022/8/3
 */
public abstract class StringToNumberCustomConverter<C, D extends Number> extends AbstractCustomConverter<String, D> {

    @Override
    public D convert(String source, Type<? extends D> destinationType, MappingContext mappingContext) {
        try {
            return convertImpl(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected abstract D convertImpl(String source);
}
