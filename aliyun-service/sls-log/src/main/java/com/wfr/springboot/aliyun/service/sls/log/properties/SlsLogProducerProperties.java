package com.wfr.springboot.aliyun.service.sls.log.properties;

import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云SLS日志服务 {@link ProducerConfig} 的属性配置
 *
 * @author wangfarui
 * @since 2022/7/7
 */
@ConfigurationProperties(prefix = "aliyun.sls.log.producer")
public class SlsLogProducerProperties {

    /**
     * 一个 ProducerBatch 从创建到可发送的逗留时间，默认为 2 秒，最小可设置成 100 毫秒。
     */
    private int lingerMs = 1000;

    /**
     * 如果某个 ProducerBatch 首次发送失败，能够对其重试的次数，默认为 10 次。
     * 如果 retries 小于等于 0，该 ProducerBatch 首次发送失败后将直接进入失败队列。
     */
    private int retries = 3;

    public int getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(int lingerMs) {
        this.lingerMs = lingerMs;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}
