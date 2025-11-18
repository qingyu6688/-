package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 每日统计表
 * 对应表：statistics_daily
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("statistics_daily")
public class StatisticsDaily {
    /**
     * 统计ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 统计日期
     */
    private LocalDate statDate;

    /**
     * 总成员数
     */
    private Integer totalMembers;

    /**
     * 活跃成员数
     */
    private Integer activeMembers;

    /**
     * 新增成员数
     */
    private Integer newMembers;

    /**
     * 总活动数
     */
    private Integer totalActivities;

    /**
     * 进行中活动数
     */
    private Integer ongoingActivities;

    /**
     * 总帖子数
     */
    private Integer totalPosts;

    /**
     * 新增帖子数
     */
    private Integer newPosts;

    /**
     * total_income
     */
    private BigDecimal totalIncome;

    /**
     * total_expense
     */
    private BigDecimal totalExpense;

    /**
     * balance
     */
    private BigDecimal balance;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
