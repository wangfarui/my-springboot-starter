package com.wfr.springboot.aliyun.service.sls.log.content;

import com.aliyun.openservices.log.common.LogItem;
import com.wfr.springboot.aliyun.service.sls.log.constants.SlsLogConstants;

import java.time.LocalTime;
import java.util.*;

/**
 * 日志信息数据
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public class SlsLogData {

    private SlsLogLever lever;

    private Map<String, Object> logContents;

    public SlsLogData() {
        this(SlsLogLever.INFO, null);
    }

    public SlsLogData(SlsLogLever lever) {
        this(lever, null);
    }

    public SlsLogData(SlsLogLever lever, Map<String, Object> logContents) {
        this.lever = lever;
        this.logContents = Optional.ofNullable(logContents).orElse(new HashMap<>());
    }

    public SlsLogData addContent(String key, Object value) {
        this.logContents.put(key, value);
        return this;
    }

    public LogItem packaging() {
        LogItem logItem = new LogItem();

        logItem.PushBack(SlsLogConstants.LOG_LEVER, this.lever.toString());

        for (Map.Entry<String, Object> entry : this.logContents.entrySet()) {
            logItem.PushBack(entry.getKey(), entry.getValue().toString());
        }

        logItem.PushBack(SlsLogConstants.LOG_END_TIME, LocalTime.now().toString());

        return logItem;
    }
}
