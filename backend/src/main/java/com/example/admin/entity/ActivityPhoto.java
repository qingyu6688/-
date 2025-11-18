package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 活动相册表
 * 对应表：activity_photo
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Data
@TableName("activity_photo")
public class ActivityPhoto {
    /**
     * 照片ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 上传者ID
     */
    private Long uploaderId;

    /**
     * 照片地址
     */
    private String photoUrl;

    /**
     * 类型(image图片/video视频)
     */
    private String photoType;

    /**
     * 描述
     */
    private String description;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否精选(0否 1是)
     */
    private Integer isFeatured;

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
     * 活动名称（用于后台显示）
     */
    @TableField(exist = false)
    private String activityName;
    
    /**
     * 上传者姓名（用于后台显示）
     */
    @TableField(exist = false)
    private String uploaderName;

}
