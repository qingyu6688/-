package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 活动统计表
 * 对应表：statistics_activity
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("statistics_activity")
public class StatisticsActivity {
    /**
     * 统计ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 报名人数
     */
    private Integer registrationCount;

    /**
     * 实际参与人数
     */
    private Integer actualParticipants;

    /**
     * 签到人数
     */
    private Integer signInCount;

    /**
     * 迟到人数
     */
    private Integer lateCount;

    /**
     * 早退人数
     */
    private Integer earlyLeaveCount;

    /**
     * 评价人数
     */
    private Integer evaluationCount;

    /**
     * average_rating
     */
    private BigDecimal averageRating;

    /**
     * total_cost
     */
    private BigDecimal totalCost;

    /**
     * total_income
     */
    private BigDecimal totalIncome;

    /**
     * participation_rate
     */
    private BigDecimal participationRate;

    /**
     * satisfaction_rate
     */
    private BigDecimal satisfactionRate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
