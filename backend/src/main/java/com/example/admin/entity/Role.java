package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色实体类（扩展）
 * 对应表：sys_role
 */
@Data
@TableName("sys_role")
public class Role {
    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色标识（唯一）
     */
    private String roleKey;
    
    /**
     * 角色类型（system系统角色/club社团角色/custom自定义角色）
     */
    private String roleType;
    
    /**
     * 角色级别（数字越大权限越高）
     */
    private Integer roleLevel;
    
    /**
     * 角色描述
     */
    private String description;
    
    /**
     * 数据范围（1全部 2自定义 3本部门 4本部门及以下 5仅本人）
     */
    private String dataScope;
    
    /**
     * 所属社团ID（社团角色专用）
     */
    private Long clubId;
    
    /**
     * 最大成员数（0表示不限）
     */
    private Integer maxMembers;
    
    /**
     * 状态（0禁用 1启用）
     */
    private Integer status;
    
    /**
     * 是否默认角色（0否 1是）
     */
    private Integer isDefault;
    
    /**
     * 显示顺序
     */
    private Integer sortOrder;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建者
     */
    private String createBy;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新者
     */
    private String updateBy;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // ========== 非数据库字段 ==========
    
    /**
     * 菜单ID列表（用于角色菜单分配）
     */
    @TableField(exist = false)
    private List<Long> menuIds;
    
    /**
     * 成员数量（统计字段）
     */
    @TableField(exist = false)
    private Integer memberCount;
}
