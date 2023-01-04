package com.wfr.springboot.plugin.dingtalk.robot;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 钉钉机器人发送消息请求对象
 * <p>对象参数名与钉钉开发平台提供的入参名保持一致</p>
 *
 * @author wangfarui
 * @since 2022/10/18
 */
public class DingTalkSendRequest {

    /**
     * 用于钉钉入参
     */
    @Getter
    private String msgtype;

    /**
     * 钉钉消息类型
     */
    @Setter
    private DingTalkRobotMsgType dingTalkRobotMsgType;

    /**
     * 钉钉消息需要 @ 的对象
     */
    @Setter
    @Getter
    private AT at;

    /**
     * 钉钉消息类型为 markdown 的内容
     */
    @Setter
    @Getter
    private Markdown markdown;

    /**
     * 钉钉消息类型为 text 的内容
     */
    @Setter
    @Getter
    private Text text;

    /**
     * 完善请求对象参数（在发送请求之前）
     */
    public boolean completeRequestParam() {
        DingTalkRobotMsgType msgType = determineMsgType();
        if (msgType == null) return false;
        this.msgtype = msgType.getType();
        if (DingTalkRobotMsgType.MARKDOWN.equals(msgType)) {
            this.getMarkdown().formatContentToText();
        }
        return true;
    }

    public void setAtAll(boolean atAll) {
        validAt();
        this.at.setAtAll(atAll);
    }

    public void setAtMobiles(List<String> atMobiles) {
        validAt();
        this.at.setAtMobiles(atMobiles);
    }

    public void setAtUserIds(List<String> atUserIds) {
        validAt();
        this.at.setAtUserIds(atUserIds);
    }

    public void setMarkdownTitle(String title) {
        validMarkdown();
        this.markdown.setTitle(title);
    }

    public void addMarkdownContent(String key, String value) {
        validMarkdown();
        this.markdown.addContent(key, value);
    }

    public void setTextContent(String content) {
        if (this.text == null) {
            this.text = new Text();
        }
        this.text.setContent(content);
    }

    private void validAt() {
        if (this.at == null) {
            this.at = new AT();
        }
    }

    private void validMarkdown() {
        if (this.markdown == null) {
            this.markdown = new Markdown();
        }
    }

    private DingTalkRobotMsgType determineMsgType() {
        if (this.markdown != null) {
            this.dingTalkRobotMsgType = DingTalkRobotMsgType.MARKDOWN;
        } else if (this.text != null) {
            this.dingTalkRobotMsgType = DingTalkRobotMsgType.TEXT;
        }
        return this.dingTalkRobotMsgType;
    }

    @Setter
    @Getter
    public static class AT {
        private List<String> atMobiles;

        private List<String> atUserIds;

        private boolean isAtAll;
    }

    public static class Markdown {
        @Setter
        @Getter
        private String title;

        @Setter
        @Getter
        private String text;

        /**
         * 非钉钉消息的数据格式
         */
        private Map<String, String> content;

        public void addContent(String key, String value) {
            if (this.content == null) {
                this.content = new HashMap<>();
            }
            this.content.put(key, value);
        }

        public void formatContentToText() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : this.content.entrySet()) {
                sb.append(entry.getKey()).append("：").append(entry.getValue()).append("\n\n");
            }
            this.text = sb.toString();
        }
    }

    @Setter
    @Getter
    public static class Text {
        private String content;
    }

    // TODO 临时toString
    @Override
    public String toString() {
        return "DingTalkSendRequest{" +
                "msgtype='" + msgtype + '\'' +
                ", dingTalkMsgType=" + dingTalkRobotMsgType +
                ", at=" + at +
                ", markdown=" + markdown +
                ", text=" + text +
                '}';
    }
}
