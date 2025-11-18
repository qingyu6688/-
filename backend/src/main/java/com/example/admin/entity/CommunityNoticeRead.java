package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 公告阅读记录表
 * 对应表：community_notice_read
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("community_notice_read")
public class CommunityNoticeRead {
    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
