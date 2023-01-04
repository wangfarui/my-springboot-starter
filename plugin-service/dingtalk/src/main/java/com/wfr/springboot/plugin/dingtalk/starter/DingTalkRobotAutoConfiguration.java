package com.wfr.springboot.plugin.dingtalk.starter;

import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.wfr.springboot.plugin.dingtalk.robot.DingTalkRobotClient;
import com.wfr.springboot.plugin.dingtalk.robot.DingTalkRobotConfigChangeListener;
import com.wfr.springboot.plugin.dingtalk.robot.DingTalkRobotProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 钉钉机器人自动装配类
 * <p>默认加载，然后支持运行时动态开关机器人</p>
 *
 * @author wangfarui
 * @since 2023/1/4
 */
@Configuration(proxyBeanMethods = false)
@Import({DingTalkRobotProperties.class, DingTalkRobotClient.class})
public class DingTalkRobotAutoConfiguration {

    @Bean
    @ConditionalOnClass(ApolloConfigChangeListener.class)
    public DingTalkRobotConfigChangeListener dingTalkConfigChangeListener() {
        return new DingTalkRobotConfigChangeListener();
    }
}
