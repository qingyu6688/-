package com.example.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.Menu;
import com.example.admin.mapper.MenuMapper;
import com.example.admin.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    
    @Override
    public List<Menu> getMenuTreeByRoleId(Long roleId) {
        List<Menu> menus = baseMapper.selectMenusByRoleId(roleId);
        return buildMenuTree(menus);
    }
    
    @Override
    public List<String> getPermsByRoleId(Long roleId) {
        return baseMapper.selectPermsByRoleId(roleId);
    }
    
    @Override
    public List<Menu> getMenuTree() {
        List<Menu> menus = baseMapper.selectMenuTree();
        return buildMenuTree(menus);
    }
    
    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> result = new ArrayList<>();
        
        // 获取所有根节点
        List<Menu> rootMenus = menus.stream()
                .filter(menu -> menu.getParentId() == 0)
                .collect(Collectors.toList());
        
        // 递归构建树形结构
        for (Menu rootMenu : rootMenus) {
            rootMenu.setChildren(getChildren(rootMenu.getId(), menus));
            result.add(rootMenu);
        }
        
        return result;
    }
    
    private List<Menu> getChildren(Long parentId, List<Menu> allMenus) {
        List<Menu> children = new ArrayList<>();
        
        for (Menu menu : allMenus) {
            if (menu.getParentId().equals(parentId)) {
                menu.setChildren(getChildren(menu.getId(), allMenus));
                children.add(menu);
            }
        }
        
        return children;
    }
}
