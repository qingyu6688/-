package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 帖子表
 * 对应表：community_post
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("community_post")
public class CommunityPost {
    /**
     * 帖子ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 板块ID
     */
    private Long categoryId;

    /**
     * 发帖人ID
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片(JSON数组)
     */
    private String images;

    /**
     * 附件(JSON数组)
     */
    private String attachments;

    /**
     * 是否置顶(0否 1是)
     */
    private Integer isTop;

    /**
     * 是否热门(0否 1是)
     */
    private Integer isHot;

    /**
     * 是否精华(0否 1是)
     */
    private Integer isEssence;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 状态(0正常 1隐藏 2删除)
     */
    private String status;

    /**
     * 审核状态(0待审核 1已通过 2未通过)
     */
    private String auditStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
