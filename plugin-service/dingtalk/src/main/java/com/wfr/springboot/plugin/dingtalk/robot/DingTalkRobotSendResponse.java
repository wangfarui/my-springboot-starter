package com.wfr.springboot.plugin.dingtalk.robot;

import lombok.Getter;
import lombok.Setter;

/**
 * 钉钉机器人发送消息响应对象
 *
 * @author wangfarui
 * @since 2022/10/18
 */
@Setter
@Getter
public class DingTalkRobotSendResponse {

    private long errcode;

    private String errmsg;
}
