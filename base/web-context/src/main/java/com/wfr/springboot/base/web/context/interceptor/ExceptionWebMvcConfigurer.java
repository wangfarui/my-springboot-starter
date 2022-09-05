package com.wfr.springboot.base.web.context.interceptor;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 异常web mvc配置器
 *
 * @author wangfarui
 * @since 2022/9/5
 */
public class ExceptionWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new ExceptionHandlerInterceptor());
    }
}
