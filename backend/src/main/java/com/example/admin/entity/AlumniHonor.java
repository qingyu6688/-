package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 荣誉记录表
 * 对应表：alumni_honor
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("alumni_honor")
public class AlumniHonor {
    /**
     * 荣誉ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 荣誉名称
     */
    private String honorName;

    /**
     * 荣誉级别(校级/市级/省级/国家级)
     */
    private String honorLevel;

    /**
     * 获奖日期
     */
    private LocalDate awardDate;

    /**
     * 颁发单位
     */
    private String issuer;

    /**
     * 证书图片
     */
    private String certificateUrl;

    /**
     * 荣誉描述
     */
    private String description;

    /**
     * 审核状态(0待审核 1已通过 2未通过)
     */
    private String auditStatus;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
