package com.example.admin.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息VO
 */
@Data
public class MessageVO {
    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 发送人ID
     */
    private Long senderId;

    /**
     * 发送人名称
     */
    private String senderName;

    /**
     * 发送人头像
     */
    private String senderAvatar;

    /**
     * 接收人ID
     */
    private Long receiverId;

    /**
     * 接收人名称
     */
    private String receiverName;

    /**
     * 接收人头像
     */
    private String receiverAvatar;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 关联ID
     */
    private Long relatedId;

    /**
     * 关联类型
     */
    private String relatedType;

    /**
     * 动作类型
     */
    private String actionType;

    /**
     * 是否已读
     */
    private Integer isRead;

    /**
     * 是否撤回
     */
    private Integer isWithdraw;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 状态（用于前端显示）
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
