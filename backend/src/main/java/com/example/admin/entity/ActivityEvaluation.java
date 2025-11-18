package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 活动评价表
 * 对应表：activity_evaluation
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("activity_evaluation")
public class ActivityEvaluation {
    /**
     * 评价ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评分(1-5星)
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片(JSON数组)
     */
    private String images;

    /**
     * 评价标签(JSON数组)
     */
    private String tags;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
