package com.wfr.springboot.base.web.context.properties;

import com.wfr.springboot.base.log.context.LogLever;
import com.wfr.springboot.base.web.context.starter.RequestLogServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.PathMatcher;

/**
 * web请求链路日志属性
 *
 * @author wangfarui
 * @since 2022/7/18
 */
@ConfigurationProperties(prefix = RequestLogServiceAutoConfiguration.WEB_REQUEST_LOG_PROPERTIES_PREFIX)
public class WebRequestLogProperties {

    /**
     * web请求链路日志的日志级别
     */
    private LogLever lever = LogLever.TRACE;

    /**
     * 拦截器的过滤属性配置
     */
    @NestedConfigurationProperty
    private FilterPatternsProperties filter = new FilterPatternsProperties();

    public LogLever getLever() {
        return lever;
    }

    public void setLever(LogLever lever) {
        this.lever = lever;
    }

    public FilterPatternsProperties getFilter() {
        return filter;
    }

    public void setFilter(FilterPatternsProperties filter) {
        this.filter = filter;
    }

    /**
     * 过滤路径属性
     */
    public static class FilterPatternsProperties {

        /**
         * 排除的路径通配符
         */
        private String[] excludePatterns;

        /**
         * 包含的路径通配符
         * <p>若该值不为空, 那么支持的路径就只能是匹配的值
         *
         * @see org.springframework.web.servlet.handler.MappedInterceptor#matches(String, PathMatcher)
         */
        private String[] includePatterns;

        public String[] getExcludePatterns() {
            return excludePatterns;
        }

        public void setExcludePatterns(String[] excludePatterns) {
            this.excludePatterns = excludePatterns;
        }

        public String[] getIncludePatterns() {
            return includePatterns;
        }

        public void setIncludePatterns(String[] includePatterns) {
            this.includePatterns = includePatterns;
        }
    }
}
