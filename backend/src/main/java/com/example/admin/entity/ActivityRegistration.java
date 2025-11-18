package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 活动报名表
 * 对应表：activity_registration
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("activity_registration")
public class ActivityRegistration {
    /**
     * 报名ID
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
     * 真实姓名
     */
    private String realName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 报名类型(0正式 1候补)
     */
    private String registrationType;

    /**
     * 审核状态(0待审核 1已通过 2未通过)
     */
    private String auditStatus;

    /**
     * 支付状态(0未支付 1已支付 2已退款)
     */
    private String paymentStatus;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // ========== 非数据库字段 ==========
    
    /**
     * 关联的活动信息（用于前端显示）
     */
    @TableField(exist = false)
    private ActivityInfo activity;
}
