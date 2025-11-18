package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 校友联系方式扩展表
 * 对应表：alumni_contact_extend
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("alumni_contact_extend")
public class AlumniContactExtend {
    /**
     * 联系方式扩展ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 工作单位
     */
    private String company;

    /**
     * 职位
     */
    private String position;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 现居住地址
     */
    private String address;

    /**
     * 紧急联系人
     */
    private String emergencyContact;

    /**
     * 紧急联系电话
     */
    private String emergencyPhone;

    /**
     * 隐私级别(0公开 1仅好友 2私密)
     */
    private Integer privacyLevel;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
