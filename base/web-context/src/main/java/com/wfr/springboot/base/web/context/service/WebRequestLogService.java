package com.wfr.springboot.base.web.context.service;

import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.service.AbstractLogService;
import com.wfr.springboot.base.web.context.properties.WebRequestLogProperties;

/**
 * web请求的日志服务
 *
 * @author wangfarui
 * @since 2022/7/15
 */
public class WebRequestLogService implements LogService {

    private final AbstractLogService delegate;

    private final WebRequestLogProperties logProperties;

    public WebRequestLogService(AbstractLogService logService, WebRequestLogProperties logProperties) {
        this.delegate = logService;
        this.logProperties = logProperties;
    }

    @Override
    public void put(LogData logData) {
        logData.logLever(logProperties.getLever());
        delegate.doPut(logData);
    }
}
