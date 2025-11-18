package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 活动签到表
 * 对应表：activity_sign_in
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("activity_sign_in")
public class ActivitySignIn {
    /**
     * 签到ID
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
     * 签到时间
     */
    private LocalDateTime signInTime;

    /**
     * 签到方式(qrcode二维码/manual手动)
     */
    private String signInType;

    /**
     * 是否迟到(0否 1是)
     */
    private Integer isLate;

    /**
     * 是否早退(0否 1是)
     */
    private Integer isEarlyLeave;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
