package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 论坛板块表
 * 对应表：community_forum_category
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("community_forum_category")
public class CommunityForumCategory {
    /**
     * 板块ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 板块名称
     */
    private String categoryName;

    /**
     * 板块图标
     */
    private String categoryIcon;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 板块描述
     */
    private String description;

    /**
     * 帖子数
     */
    private Integer postCount;

    /**
     * 状态(0正常 1停用)
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
