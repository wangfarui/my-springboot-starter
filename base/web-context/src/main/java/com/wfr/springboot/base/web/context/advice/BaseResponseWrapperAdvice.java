package com.wfr.springboot.base.web.context.advice;

import com.wfr.base.framework.common.BaseResponse;
import com.wfr.springboot.base.json.mapper.JsonMapper;
import com.wfr.springboot.base.log.context.LogContext;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
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
        // TODO 暂时支持对所有返回类型做包装处理, 后期改为集合遍历判断
        return true;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(@Nullable Object body, @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        BaseResponse<Object> baseResponse;
        if (returnType.getParameterType().isAssignableFrom(WRAPPER_CLASS)) {
            baseResponse = (BaseResponse<Object>) body;
            if (baseResponse == null) {
                baseResponse = BaseResponse.success();
            }
            if (baseResponse.getTraceId() == null) {
                baseResponse.setTraceId(LogContext.getTraceId());
            }
        } else {
            if (isBasicError(returnType.getDeclaringClass())) {
                baseResponse = BaseResponse.fail();
                baseResponse.setData(body);
            } else {
                baseResponse = BaseResponse.success(body);
            }
            baseResponse.setTraceId(LogContext.getTraceId());
        }
        if (selectedContentType.isCompatibleWith(MediaType.TEXT_PLAIN)) {
            return JsonMapper.toJson(baseResponse);
        }
        return baseResponse;
    }

    private boolean isBasicError(Class<?> declaringClass) {
        return declaringClass == BasicErrorController.class;
    }
}
