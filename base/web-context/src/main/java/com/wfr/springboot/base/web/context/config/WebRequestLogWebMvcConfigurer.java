package com.wfr.springboot.base.web.context.config;

import com.wfr.springboot.base.web.context.interceptor.WebRequestLogHandlerInterceptor;
import com.wfr.springboot.base.web.context.properties.WebRequestLogProperties;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web日志相关的web配置
 *
 * @author wangfarui
 * @since 2022/7/19
 */
public class WebRequestLogWebMvcConfigurer implements WebMvcConfigurer {

    private final WebRequestLogProperties.FilterPatternsProperties filterPatternsProperties;

    public WebRequestLogWebMvcConfigurer(WebRequestLogProperties.FilterPatternsProperties filterPatternsProperties) {
        this.filterPatternsProperties = filterPatternsProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new WebRequestLogHandlerInterceptor()).order(1);
        if (!ObjectUtils.isEmpty(this.filterPatternsProperties.getExcludePatterns())) {
            interceptorRegistration.excludePathPatterns(this.filterPatternsProperties.getExcludePatterns());
        }
        if (!ObjectUtils.isEmpty(this.filterPatternsProperties.getIncludePatterns())) {
            interceptorRegistration.addPathPatterns(this.filterPatternsProperties.getIncludePatterns());
        }
    }
}
