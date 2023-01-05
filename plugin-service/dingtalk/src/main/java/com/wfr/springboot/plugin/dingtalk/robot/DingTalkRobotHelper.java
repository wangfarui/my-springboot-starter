package com.wfr.springboot.plugin.dingtalk.robot;

import com.wfr.base.framework.common.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉机器人辅助器
 *
 * @author wangfarui
 * @since 2022/10/18
 */
@Slf4j
public abstract class DingTalkRobotHelper {

    private static DingTalkRobotClient DING_TALK_CLIENT;

    public static void setDingTalkClient(DingTalkRobotClient dingTalkRobotClient) {
        if (DING_TALK_CLIENT == null) {
            DING_TALK_CLIENT = dingTalkRobotClient;
        }
    }

    private static DingTalkRobotClient getDingTalkClient() {
        if (DING_TALK_CLIENT == null) {
            throw new IllegalStateException("钉钉客户端加载异常");
        }
        return DING_TALK_CLIENT;
    }

    public static void send(DingTalkRobotSendRequest request) {
        getDingTalkClient().send(request);
    }

    public static void sendMessage(String message) {
        DingTalkRobotSendRequest request = new DingTalkRobotSendRequest();
        request.setTextContent(message);
        getDingTalkClient().send(request);
    }

    public static void sendException(String message, Throwable e) {
        DingTalkRobotSendRequest request = new DingTalkRobotSendRequest();
        request.setMarkdownTitle("自定义异常");
        request.addMarkdownContent("异常内容", message);
        request.addMarkdownContent("异常信息", ExceptionUtils.exceptionStackTraceText(e));
        getDingTalkClient().send(request);
    }

}
