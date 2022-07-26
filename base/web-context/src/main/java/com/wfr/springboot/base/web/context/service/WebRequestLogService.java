package com.wfr.springboot.base.web.context.service;

import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.interceptor.LogInterceptor;
import com.wfr.springboot.base.log.context.service.AbstractLogService;

import java.util.ArrayList;
import java.util.List;

/**
 * web请求的日志服务
 *
 * @author wangfarui
 * @since 2022/7/15
 */
public class WebRequestLogService implements LogService {

    private final AbstractLogService delegate;

    private final List<LogInterceptor> logInterceptors = new ArrayList<>();

    public WebRequestLogService(AbstractLogService logService) {
        this.delegate = logService;
    }

    @Override
    public void push(LogData logData) {
        this.delegate.push(logData, true);
    }

}
