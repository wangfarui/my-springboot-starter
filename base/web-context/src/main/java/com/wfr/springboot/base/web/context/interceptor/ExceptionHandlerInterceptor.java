package com.wfr.springboot.base.web.context.interceptor;

import com.wfr.base.framework.common.BaseResponse;
import com.wfr.base.framework.common.CommonApiCode;
import com.wfr.springboot.base.json.mapper.JsonMapper;
import com.wfr.springboot.base.log.context.LogContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理器拦截器
 *
 * @author wangfarui
 * @since 2022/9/5
 */
public class ExceptionHandlerInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, @Nullable Exception ex) throws Exception {
        if (ex == null) {
            return;
        }

        CommonApiCode serverError = CommonApiCode.SERVER_ERROR;
        response.setStatus(serverError.getCode());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding("utf-8");

        BaseResponse<Object> baseResponse = new BaseResponse<>();
        baseResponse.setCode(serverError.getCode());
        baseResponse.setMessage(ex.getMessage());
        baseResponse.setTraceId(LogContext.getTraceId());

        response.getWriter().write(JsonMapper.toJson(baseResponse));
    }
}
