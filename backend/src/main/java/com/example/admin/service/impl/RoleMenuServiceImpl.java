package com.example.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.RoleMenu;
import com.example.admin.mapper.RoleMenuMapper;
import com.example.admin.service.RoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenusToRole(Long roleId, List<Long> menuIds) {
        // 先删除该角色的所有菜单关联
        baseMapper.deleteByRoleId(roleId);
        
        // 如果有新的菜单ID，则批量插入
        if (menuIds != null && !menuIds.isEmpty()) {
            baseMapper.insertBatch(roleId, menuIds);
        }
    }
    
    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return baseMapper.selectMenuIdsByRoleId(roleId);
    }
}
