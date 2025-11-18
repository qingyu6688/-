package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 社团经历表
 * 对应表：alumni_club_history
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("alumni_club_history")
public class AlumniClubHistory {
    /**
     * 经历ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 社团名称
     */
    private String clubName;

    /**
     * 担任职务
     */
    private String position;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 经历描述
     */
    private String description;

    /**
     * 是否当前职务(0否 1是)
     */
    private Integer isCurrent;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
