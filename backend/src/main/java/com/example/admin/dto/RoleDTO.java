package com.example.admin.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色DTO（数据传输对象）
 */
@Data
public class RoleDTO {
    
    private Long id;
    
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
    
    @NotBlank(message = "角色标识不能为空")
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
    
    /**
     * 菜单ID列表（用于角色菜单分配）
     */
    private List<Long> menuIds;
}
