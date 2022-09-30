package com.wfr.springboot.base.log.context.interceptor;

import com.wfr.springboot.base.log.context.LogConstants;
import com.wfr.springboot.base.log.context.LogContext;
import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogService;
import org.springframework.core.Ordered;

/**
 * 基本信息填充者 日志拦截器
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public class BasicInformationFillerLogInterceptor implements LogInterceptor, Ordered {

    public static final int ORDER_PRECEDENCE = Ordered.LOWEST_PRECEDENCE;

    @Override
    public void processBeforePushLog(LogService logService, LogData logData) {
        long useTime = System.currentTimeMillis() - logData.getStartTime();
        logData.add(LogConstants.LOG_LEVEL, logData.getLogLever());
        logData.add(LogConstants.LOG_USE_TIME, useTime + "ms");
    }

    @Override
    public int getOrder() {
        return ORDER_PRECEDENCE;
    }
}
