package com.wfr.springboot.base.log.context.service;

import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogLever;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.interceptor.LogInterceptor;
import com.wfr.springboot.base.log.context.properties.LogContextProperties;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 日志服务抽象层
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public abstract class AbstractLogService implements LogService {

    /**
     * 默认日志服务Bean名称
     * <p>默认日志要求必须继承 {@link AbstractLogService}
     */
    public static final String DEFAULT_LOG_SERVICE = "defaultLogService";

    /**
     * 日志上下文属性配合
     */
    private final LogContextProperties logContextProperties;

    /**
     * 日志拦截器
     * <p>默认不校验拦截器是否重复
     */
    private final List<LogInterceptor> logInterceptors;

    public AbstractLogService() {
        this(null, null);
    }

    public AbstractLogService(@NonNull List<LogInterceptor> logInterceptors) {
        this(null, logInterceptors);
    }

    public AbstractLogService(@Nullable LogContextProperties logContextProperties,
                              @Nullable List<LogInterceptor> logInterceptors) {
        this.logContextProperties = Optional.ofNullable(logContextProperties)
                .orElse(LogContextProperties.DEFAULT_LOG_CONTEXT_PROPERTIES);
        this.logInterceptors = Optional.ofNullable(logInterceptors).orElse(new ArrayList<>());
    }

    @Override
    public LogLever logLever() {
        return logContextProperties.getLever();
    }

    @Override
    public void put(LogData logData) {
        if (!canPut(logData.getLogLever())) {
            return;
        }
        logInterceptors.forEach(logInterceptor -> logInterceptor.processBeforePutLog(this, logData));
        doPut(logData);
        logInterceptors.forEach(logInterceptor -> logInterceptor.processAfterPutLog(this, logData));
    }

    /**
     * 执行推送日志操作
     *
     * @param logData 日志信息
     */
    public void doPut(LogData logData) {
    }

    public void addLogInterceptor(LogInterceptor logInterceptor) {
        this.logInterceptors.add(logInterceptor);
    }
}
