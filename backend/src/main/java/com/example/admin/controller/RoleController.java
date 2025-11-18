package com.example.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.common.Result;
import com.example.admin.entity.Role;
import com.example.admin.service.RoleMenuService;
import com.example.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private RoleMenuService roleMenuService;
    
    /**
     * 分页查询角色列表
     */
    @GetMapping("/list")
    public Result<Page<Role>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String roleName) {
        
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (roleName != null && !roleName.isEmpty()) {
            queryWrapper.like("role_name", roleName);
        }
        
        Page<Role> page = new Page<>(pageNum, pageSize);
        Page<Role> result = roleService.page(page, queryWrapper);
        return Result.success(result);
    }
    
    /**
     * 查询所有角色
     */
    @GetMapping("/all")
    public Result<List<Role>> getAllRoles() {
        List<Role> roles = roleService.list();
        return Result.success(roles);
    }
    
    /**
     * 新增角色
     */
    @PostMapping
    public Result<Void> add(@RequestBody Role role) {
        try {
            roleService.save(role);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改角色
     */
    @PutMapping
    public Result<Void> update(@RequestBody Role role) {
        try {
            roleService.updateById(role);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            roleService.removeById(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 为角色分配菜单权限
     */
    @PostMapping("/assignMenus")
    public Result<Void> assignMenus(@RequestBody Map<String, Object> params) {
        try {
            Long roleId = Long.valueOf(params.get("roleId").toString());
            @SuppressWarnings("unchecked")
            List<Long> menuIds = (List<Long>) params.get("menuIds");
            roleMenuService.assignMenusToRole(roleId, menuIds);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 查询角色已分配的菜单ID列表
     */
    @GetMapping("/menuIds/{roleId}")
    public Result<List<Long>> getMenuIds(@PathVariable Long roleId) {
        try {
            List<Long> menuIds = roleMenuService.getMenuIdsByRoleId(roleId);
            return Result.success(menuIds);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
