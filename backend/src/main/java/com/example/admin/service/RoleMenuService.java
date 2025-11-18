package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.RoleMenu;

import java.util.List;

public interface RoleMenuService extends IService<RoleMenu> {
    
    /**
     * 为角色分配菜单权限
     */
    void assignMenusToRole(Long roleId, List<Long> menuIds);
    
    /**
     * 根据角色ID查询菜单ID列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);
}
