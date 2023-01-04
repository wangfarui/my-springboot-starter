package com.wfr.springboot.plugin.dingtalk.robot;

/**
 * 钉钉机器人消息类型
 *
 * @author wangfarui
 * @since 2022/10/18
 */
public enum DingTalkRobotMsgType {
    TEXT("text"),
    LINK("link"),
    MARKDOWN("markdown"),
    ACTION_CARD("actionCard"),
    FEED_CARD("feedCard");

    private final String type;

    DingTalkRobotMsgType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
