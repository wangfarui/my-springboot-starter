package com.wfr.springboot.aliyun.service.sls.log.service;

import com.aliyun.openservices.aliyun.log.producer.LogProducer;
import com.aliyun.openservices.aliyun.log.producer.Producer;
import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
import com.aliyun.openservices.log.common.LogItem;
import com.wfr.springboot.aliyun.service.sls.log.SlsLogData;
import com.wfr.springboot.aliyun.service.sls.log.properties.SlsLogProducerProperties;
import com.wfr.springboot.aliyun.service.sls.log.properties.SlsLogProjectProperties;
import com.wfr.springboot.base.log.context.LogContent;
import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.interceptor.LogInterceptor;
import com.wfr.springboot.base.log.context.properties.LogContextProperties;
import com.wfr.springboot.base.log.context.service.AbstractLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 阿里云SLS日志服务 默认实现类
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public class SlsLogService extends AbstractLogService implements InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlsLogService.class);

    private Producer producer;

    private final SlsLogProjectProperties projectProperties;

    private final SlsLogProducerProperties producerProperties;

    private final String project;

    private final String logStore;

    public SlsLogService(LogContextProperties logContextProperties,
                         List<LogInterceptor> logInterceptors,
                         SlsLogProjectProperties projectProperties,
                         SlsLogProducerProperties producerProperties) {
        super(logContextProperties, logInterceptors);
        this.projectProperties = projectProperties;
        this.producerProperties = producerProperties;

        this.project = projectProperties.getProject();
        this.logStore = projectProperties.getLogStore();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.producer == null) {
            ProducerConfig producerConfig = new ProducerConfig();
            fillProducerConfig(producerConfig);

            LogProducer logProducer = new LogProducer(producerConfig);
            fillLogProducer(logProducer);

            this.producer = logProducer;
        }
    }

    @Override
    public void destroy() throws Exception {
        if (this.producer != null) {
            this.producer.close();
            this.producer = null;
        }
    }

    @Override
    public void doPush(LogData logData) {
        LogItem logItem = packaging(logData);
        List<LogItem> logItems = Collections.singletonList(logItem);
        try {
            if (logData instanceof SlsLogData) {
                SlsLogData slsLogData = (SlsLogData) logData;
                this.producer.send(this.project, this.logStore, slsLogData.getTopic(), slsLogData.getSource(), logItems);
            } else {
                this.producer.send(this.project, this.logStore, logItems);
            }
        } catch (Throwable e) {
            LOGGER.error("Sls log sending exception", e);
        }
    }

    private LogItem packaging(LogData logData) {
        LogItem logItem = new LogItem();

        for (LogContent.LogEntry entry : logData.getLogContents().logEntryList()) {
            logItem.PushBack(entry.getKey(), entry.getValueStr());
        }

        return logItem;
    }

    private void fillProducerConfig(ProducerConfig producerConfig) {
        SlsLogProducerProperties properties = this.producerProperties;
        if (properties.getLingerMs() >= ProducerConfig.LINGER_MS_LOWER_LIMIT) {
            producerConfig.setLingerMs(properties.getLingerMs());
        }
        producerConfig.setRetries(properties.getRetries());
    }

    private void fillLogProducer(LogProducer logProducer) {
        ProjectConfig projectConfig = SlsLogProjectProperties.generateProjectConfig(this.projectProperties);
        logProducer.putProjectConfig(projectConfig);
    }
}
