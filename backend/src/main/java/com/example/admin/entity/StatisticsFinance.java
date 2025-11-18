package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 财务统计表
 * 对应表：statistics_finance
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("statistics_finance")
public class StatisticsFinance {
    /**
     * 统计ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 统计月份(YYYY-MM)
     */
    private String statMonth;

    /**
     * total_income
     */
    private BigDecimal totalIncome;

    /**
     * total_expense
     */
    private BigDecimal totalExpense;

    /**
     * membership_fee
     */
    private BigDecimal membershipFee;

    /**
     * sponsorship
     */
    private BigDecimal sponsorship;

    /**
     * activity_income
     */
    private BigDecimal activityIncome;

    /**
     * activity_expense
     */
    private BigDecimal activityExpense;

    /**
     * material_expense
     */
    private BigDecimal materialExpense;

    /**
     * venue_expense
     */
    private BigDecimal venueExpense;

    /**
     * other_income
     */
    private BigDecimal otherIncome;

    /**
     * other_expense
     */
    private BigDecimal otherExpense;

    /**
     * balance
     */
    private BigDecimal balance;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
