package com.example.admin.controller;

import com.example.admin.common.Result;
import com.example.admin.entity.Menu;
import com.example.admin.entity.User;
import com.example.admin.service.MenuService;
import com.example.admin.service.UserService;
import com.example.admin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 查询菜单树
     */
    @GetMapping("/tree")
    public Result<List<Menu>> getMenuTree() {
        try {
            List<Menu> menuTree = menuService.getMenuTree();
            return Result.success(menuTree);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取当前用户的权限标识
     */
    @GetMapping("/permissions")
    public Result<List<String>> getUserPermissions(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || token.trim().isEmpty()) {
                return Result.success(List.of());
            }
            
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            String username = jwtUtil.getUsernameFromToken(token);
            if (username == null || username.trim().isEmpty()) {
                return Result.success(List.of());
            }
            
            User user = userService.getUserByUsername(username);
            
            if (user != null && user.getRoleId() != null) {
                List<String> permissions = menuService.getPermsByRoleId(user.getRoleId());
                return Result.success(permissions);
            }
            return Result.success(List.of());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.success(List.of());
        }
    }
    
    /**
     * 根据角色ID查询菜单树
     */
    @GetMapping("/tree/{roleId}")
    public Result<List<Menu>> getMenuTreeByRoleId(@PathVariable Long roleId) {
        try {
            List<Menu> menuTree = menuService.getMenuTreeByRoleId(roleId);
            return Result.success(menuTree);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 新增菜单
     */
    @PostMapping
    public Result<Void> add(@RequestBody Menu menu) {
        try {
            menuService.save(menu);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改菜单
     */
    @PutMapping
    public Result<Void> update(@RequestBody Menu menu) {
        try {
            menuService.updateById(menu);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            menuService.removeById(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
