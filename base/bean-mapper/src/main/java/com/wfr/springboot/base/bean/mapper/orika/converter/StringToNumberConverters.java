package com.wfr.springboot.base.bean.mapper.orika.converter;

import com.wfr.springboot.base.bean.mapper.orika.starter.OrikaBeanMapperAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * {@link String} 转 {@link Number} 子类型的转换器
 * <p>除 8种基础类型以及包装类型 + 枚举类型 以外的Number类型</p>
 * <ul>
 *     <li>java.math.BigDecimal</li>
 *     <li>java.math.BigInteger</li>
 * </ul>
 *
 * @author wangfarui
 * @since 2022/8/3
 */
@Configuration(proxyBeanMethods = false)
public class StringToNumberConverters implements Ordered {

    @Override
    public int getOrder() {
        return OrikaBeanMapperAutoConfiguration.ORDER_VALUE - 10;
    }

    @Component
    public static class StringToBigDecimalConverter extends StringToNumberCustomConverter<String, BigDecimal> {

        @Override
        protected BigDecimal convertImpl(String source) {
            return new BigDecimal(source);
        }
    }

    @Component
    public static class StringToBigIntConverter extends StringToNumberCustomConverter<String, BigInteger> {

        @Override
        protected BigInteger convertImpl(String source) {
            return new BigInteger(source);
        }
    }

}
