package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 点赞记录表
 * 对应表：community_like
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("community_like")
public class CommunityLike {
    /**
     * 点赞ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 目标类型(post帖子/comment评论/moment动态)
     */
    private String targetType;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 点赞人ID
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
