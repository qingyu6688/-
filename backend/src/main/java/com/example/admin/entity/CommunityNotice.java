package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 通知公告表
 * 对应表：community_notice
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("community_notice")
public class CommunityNotice {
    /**
     * 公告ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告类型(urgent紧急/important重要/normal一般)
     */
    private String noticeType;

    /**
     * 是否置顶(0否 1是)
     */
    private Integer isTop;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 发布人ID
     */
    private Long publisherId;

    /**
     * 目标用户(JSON数组,为空表示全部)
     */
    private String targetUsers;

    /**
     * 已读人数
     */
    private Integer readCount;

    /**
     * 状态(0草稿 1已发布 2已撤回)
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
