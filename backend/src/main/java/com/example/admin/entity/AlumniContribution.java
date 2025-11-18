package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 贡献度记录表
 * 对应表：alumni_contribution
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("alumni_contribution")
public class AlumniContribution {
    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 贡献类型(activity活动/post发帖/volunteer志愿/comment评论)
     */
    private String contributionType;

    /**
     * 贡献值
     */
    private Integer contributionValue;

    /**
     * 关联ID
     */
    private Long relatedId;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
