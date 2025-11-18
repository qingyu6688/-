package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 评论表
 * 对应表：community_comment
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("community_comment")
public class CommunityComment {
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 评论人ID
     */
    private Long userId;

    /**
     * 父评论ID(0为一级评论)
     */
    private Long parentId;

    /**
     * 回复的评论ID
     */
    private Long replyToId;

    /**
     * 回复的用户ID
     */
    private Long replyToUserId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 图片URL列表(JSON格式)
     */
    private String images;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 状态(0正常 1隐藏 2删除)
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
