package com.wfr.springboot.base.log.context.service;

import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.interceptor.LogInterceptor;
import com.wfr.springboot.base.log.context.properties.LogContextProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 通用日志服务
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public class GenericLogService extends AbstractLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericLogService.class);

    public GenericLogService(LogContextProperties logContextProperties, List<LogInterceptor> logInterceptors) {
        super(logContextProperties, logInterceptors);
    }

    @Override
    public void doPush(LogData logData) {
        String contentStr = logData.formatLogContents();
        switch (logData.getLogLever()) {
            case TRACE:
                LOGGER.trace(contentStr);
                break;
            case DEBUG:
                LOGGER.debug(contentStr);
                break;
            case INFO:
                LOGGER.info(contentStr);
                break;
            case WARN:
                LOGGER.warn(contentStr);
                break;
            case ERROR:
                LOGGER.error(contentStr);
                break;
            default:
                LOGGER.warn("无法识别的日志级别, 请检查LogLevel枚举值");
        }
    }
}
