package com.wfr.springboot.plugin.dingtalk.robot;

import cn.hutool.http.Method;
import com.wfr.base.framework.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 钉钉机器人客户端
 *
 * @author wangfarui
 * @since 2022/10/18
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DingTalkRobotClient {

    private final DingTalkRobotProperties properties;

    private static boolean CAN_APPLY;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    @SuppressWarnings("all")
    public DingTalkRobotClient(DingTalkRobotProperties properties) {
        this.properties = properties;
        changeCanApply(properties.isEnabled());
        DingTalkRobotHelper.setDingTalkClient(this);
    }

    /**
     * 发送钉钉消息
     *
     * @param request 机器人消息对象
     */
    public void send(final DingTalkRobotSendRequest request) {
        send(request, CAN_APPLY);
    }

    /**
     * 发送钉钉消息
     *
     * @param request 机器人消息对象
     */
    public void send(final DingTalkRobotSendRequest request, boolean canApply) {
        if (canApply) {
            boolean completed = request.completeRequestParam();
            if (!completed) {
                log.warn("[DingTalkClient][send]钉钉消息请求对象数据异常, request:{}", request);
            }
            final String requestUrl = properties.getRequestUrl();
            EXECUTOR_SERVICE.execute(() -> {
                DingTalkRobotSendResponse response = HttpUtils.createRequest()
                        .setMethod(Method.POST)
                        .setUrl(requestUrl)
                        .setBody(request)
                        .setCharset(StandardCharsets.UTF_8)
                        .executePost(DingTalkRobotSendResponse.class);
                if (response == null) {
                    log.warn("[DingTalkClient][send]发送钉钉消息异常, request:{}", request);
                } else if (response.getErrcode() != 0) {
                    log.warn("[DingTalkClient][send]发送钉钉消息失败, request:{}, response:{}", request, response);
                }
            });
        }
    }

    public static void changeCanApply(boolean canApply) {
        CAN_APPLY = canApply;
    }
}
