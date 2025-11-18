package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 活动信息表
 * 对应表：activity_info
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("activity_info")
public class ActivityInfo {
    /**
     * 活动ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动类型(lecture讲座/competition比赛/party聚会/volunteer公益/training培训/exhibition展览)
     */
    private String activityType;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 最大参与人数(0表示不限)
     */
    private Integer maxParticipants;

    /**
     * 当前参与人数
     */
    private Integer currentParticipants;

    /**
     * 报名截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime registrationDeadline;

    /**
     * fee
     */
    private BigDecimal fee;

    /**
     * 组织者ID
     */
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private Long organizerId;

    /**
     * 活动状态(0报名中 1进行中 2已结束 3已取消)
     */
    private String status;

    /**
     * 审核状态(0待审核 1已通过 2未通过)
     */
    private String auditStatus;

    /**
     * 是否需要审核报名(0否 1是)
     */
    private Integer needAudit;

    /**
     * 活动标签(JSON数组)
     */
    private String tags;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
