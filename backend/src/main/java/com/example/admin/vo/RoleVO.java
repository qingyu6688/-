package com.example.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色VO（视图对象）
 */
@Data
public class RoleVO {
    private Long id;
    private String roleName;
    private String roleKey;
    private String roleType;
    private Integer roleLevel;
    private String description;
    private String dataScope;
    private Long clubId;
    private Integer maxMembers;
    private Integer status;
    private Integer isDefault;
    private Integer sortOrder;
    private String remark;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    
    /**
     * 成员数量（统计字段）
     */
    private Integer memberCount;
    
    /**
     * 菜单ID列表
     */
    private List<Long> menuIds;
}
