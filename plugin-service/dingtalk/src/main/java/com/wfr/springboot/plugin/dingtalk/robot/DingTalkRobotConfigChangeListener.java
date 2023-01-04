package com.wfr.springboot.plugin.dingtalk.robot;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 钉钉机器人配置信息变更监听器
 *
 * @author wangfarui
 * @since 2022/10/18
 */
@Configuration(proxyBeanMethods = false)
public class DingTalkRobotConfigChangeListener {

    @Resource
    private DingTalkRobotClient dingTalkRobotClient;

    @ApolloConfigChangeListener(interestedKeyPrefixes = {"dingTalk"})
    private void onChange(ConfigChangeEvent changeEvent) {
        DingTalkRobotProperties.changeProperties(changeEvent);

        String key = "dingTalk.enabled";
        if (changeEvent.isChanged(key)) {
            ConfigChange change = changeEvent.getChange(key);
            String newValue = change.getNewValue();
            boolean b = Boolean.parseBoolean(newValue);
            DingTalkRobotClient.changeCanApply(b);
            pushToDingTalk(b);
        }
    }

    private void pushToDingTalk(boolean b) {
        DingTalkSendRequest request = new DingTalkSendRequest();
        request.setDingTalkRobotMsgType(DingTalkRobotMsgType.TEXT);
        request.setTextContent("钉钉告警已" + (b ? "开启" : "关闭"));
        dingTalkRobotClient.send(request, true);
    }

}
