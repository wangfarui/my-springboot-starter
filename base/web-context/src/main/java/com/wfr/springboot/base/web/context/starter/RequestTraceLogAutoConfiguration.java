package com.wfr.springboot.base.web.context.starter;

import com.wfr.springboot.base.web.context.http.HttpRequestTraceLogFilter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * web请求链路日志的自动装配
 *
 * <p>请求链路日志类型</p>
 * <ul>
 *     <li>http</li>
 *     <li>feign</li>
 *     <li>http utils</li>
 * </ul>
 *
 * @author wangfarui
 * @since 2022/7/21
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RequestLogServiceAutoConfiguration.class)
public class RequestTraceLogAutoConfiguration {

    /**
     * http请求链路日志自动装配
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Import({HttpRequestTraceLogFilter.class})
    static class HttpRequestTraceLogAutoConfiguration {

    }

}
