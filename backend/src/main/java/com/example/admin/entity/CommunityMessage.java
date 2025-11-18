package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 消息表
 * 对应表：community_message
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("community_message")
public class CommunityMessage {
    /**
     * 消息ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息类型(system系统/chat聊天/notice通知)
     */
    private String messageType;

    /**
     * 发送人ID
     */
    private Long senderId;

    /**
     * 接收人ID
     */
    private Long receiverId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 内容类型(text文字/image图片/file文件)
     */
    private String contentType;

    /**
     * 关联ID
     */
    private Long relatedId;

    /**
     * 是否已读(0未读 1已读)
     */
    private Integer isRead;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 是否撤回(0否 1是)
     */
    private Integer isWithdraw;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
