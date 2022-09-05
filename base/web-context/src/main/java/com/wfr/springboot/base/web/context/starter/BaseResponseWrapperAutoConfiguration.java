package com.wfr.springboot.base.web.context.starter;

import com.wfr.base.framework.common.BaseResponse;
import com.wfr.springboot.base.web.context.advice.BaseResponseWrapperAdvice;
import com.wfr.springboot.base.web.context.interceptor.ExceptionWebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * {@link BaseResponse} 包装器自动装配
 *
 * @author wangfarui
 * @since 2022/9/5
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(value = {BaseResponse.class, ResponseBodyAdvice.class})
@Import({BaseResponseWrapperAdvice.class, ExceptionWebMvcConfigurer.class})
public class BaseResponseWrapperAutoConfiguration {

}
