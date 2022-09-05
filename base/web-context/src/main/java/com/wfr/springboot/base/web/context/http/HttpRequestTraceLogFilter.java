package com.wfr.springboot.base.web.context.http;

import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogLever;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.Logger;
import com.wfr.springboot.base.web.context.properties.WebRequestLogProperties;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * http请求日志过滤器
 *
 * @author wangfarui
 * @since 2022/7/21
 */
public class HttpRequestTraceLogFilter extends OncePerRequestFilter implements InitializingBean {

    @Nullable
    private final LogService logService;

    private final WebRequestLogProperties logProperties;

    private final Charset CHARSET = Charset.defaultCharset();

    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HttpRequestTraceLogFilter.class);

    private Set<String> excludePatternSet = new HashSet<>();

    private Set<String> includePatternSet = new HashSet<>();

    public HttpRequestTraceLogFilter(@Nullable LogService logService, WebRequestLogProperties logProperties) {
        this.logService = logService;
        this.logProperties = Optional.ofNullable(logProperties).orElse(new WebRequestLogProperties());
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        WebRequestLogProperties.FilterPatternsProperties filterPatternsProperties = this.logProperties.getFilter();
        if (filterPatternsProperties != null) {
            if (filterPatternsProperties.getExcludePatterns() != null) {
                this.excludePatternSet = Arrays.stream(filterPatternsProperties.getExcludePatterns()).collect(Collectors.toSet());
            }
            if (filterPatternsProperties.getIncludePatterns() != null) {
                this.includePatternSet = Arrays.stream(filterPatternsProperties.getIncludePatterns()).collect(Collectors.toSet());
            }
        }
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (logService == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String method = request.getMethod();
        if (HttpMethod.OPTIONS.matches(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURI();
        boolean isSkipFilter = skipRequestTraceLogFilter(requestURI);
        if (isSkipFilter) {
            filterChain.doFilter(request, response);
            return;
        }

        LogData logData = LogData.createLogData(logProperties.getLever())
                .add(HttpRequestTraceLogKeyConstants.REQUEST_METHOD, method)
                .add(HttpRequestTraceLogKeyConstants.REQUEST_URI, requestURI);

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (Throwable e) {
            logData.logLever(LogLever.ERROR);
            logData.addException(e);
        } finally {
            try {
                String queryString = requestWrapper.getQueryString();
                if (StringUtils.hasLength(queryString)) {
                    logData.add(HttpRequestTraceLogKeyConstants.REQUEST_PARAM,
                            URLDecoder.decode(queryString, StandardCharsets.UTF_8.name()));
                }
                if (!HttpMethod.GET.matches(method)) {
                    try {
                        ServletInputStream inputStream = requestWrapper.getInputStream();
                        while (!inputStream.isFinished()) {
                            inputStream.read();
                        }
                    } catch (IOException e) {
                        LOGGER.error("读取请求body数据时IO异常", e);
                    }
                    String body = IOUtils.toString(requestWrapper.getContentAsByteArray(), CHARSET.name());
                    logData.add(HttpRequestTraceLogKeyConstants.REQUEST_BODY, body);
                }
                logData.add(HttpRequestTraceLogKeyConstants.REQUEST_RESPONSE_STATUS, responseWrapper.getStatus());
                String responseBody = IOUtils.toString(responseWrapper.getContentAsByteArray(), CHARSET.name());
                logData.add(HttpRequestTraceLogKeyConstants.REQUEST_RESPONSE_BODY, responseBody);
                Logger.push(logData, this.logService);
            } catch (Throwable throwable) {
                LOGGER.error("写入http请求日志异常", throwable);
            }
            responseWrapper.copyBodyToResponse();
        }
    }

    public void addExcludePatterns(Collection<? extends String> excludePatterns) {
        this.excludePatternSet.addAll(excludePatterns);
    }

    public void addIncludePatterns(Collection<? extends String> includePatterns) {
        this.includePatternSet.addAll(includePatterns);
    }

    /**
     * TODO 后期改用match正则匹配
     *
     * @param requestURI 请求路径
     * @return 跳过请求链路日志拦截器
     */
    private boolean skipRequestTraceLogFilter(String requestURI) {
        ok:
        if (!this.includePatternSet.isEmpty()) {
            if (this.includePatternSet.contains(requestURI)) {
                break ok;
            }
            return true;
        }
        return this.excludePatternSet.isEmpty() || this.excludePatternSet.contains(requestURI);
    }
}
