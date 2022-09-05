package com.wfr.springboot.base.web.context.advice;

import com.wfr.base.framework.common.BaseResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * {@link BaseResponse} 包装器Advice
 *
 * @author wangfarui
 * @since 2022/9/5
 */
@ControllerAdvice
@Order
public class BaseResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    private static final Class<?> WRAPPER_CLASS = BaseResponse.class;

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getParameterType().isAssignableFrom(WRAPPER_CLASS);
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        return BaseResponse.success(body);
    }
}
