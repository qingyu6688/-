package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 成员统计表
 * 对应表：statistics_member
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("statistics_member")
public class StatisticsMember {
    /**
     * 统计ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 参与活动数
     */
    private Integer activityCount;

    /**
     * 发帖数
     */
    private Integer postCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * volunteer_hours
     */
    private BigDecimal volunteerHours;

    /**
     * 贡献值
     */
    private Integer contributionValue;

    /**
     * 活跃天数
     */
    private Integer activeDays;

    /**
     * 最后活跃时间
     */
    private LocalDateTime lastActiveTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
