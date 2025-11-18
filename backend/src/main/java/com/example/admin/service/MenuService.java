package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {
    
    /**
     * 根据角色ID查询菜单树
     */
    List<Menu> getMenuTreeByRoleId(Long roleId);
    
    /**
     * 根据角色ID查询权限标识
     */
    List<String> getPermsByRoleId(Long roleId);
    
    /**
     * 查询所有菜单树
     */
    List<Menu> getMenuTree();
    
    /**
     * 构建树形结构
     */
    List<Menu> buildMenuTree(List<Menu> menus);
}
