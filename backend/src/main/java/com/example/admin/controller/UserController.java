package com.example.admin.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.annotation.Log;
import com.example.admin.annotation.RepeatSubmit;
import com.example.admin.common.Result;
import com.example.admin.dto.LoginDTO;
import com.example.admin.dto.RegisterDTO;
import com.example.admin.entity.User;
import com.example.admin.service.LoginLogService;
import com.example.admin.service.UserService;
import com.example.admin.util.JwtUtil;
import com.example.admin.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private LoginLogService loginLogService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 刷新Token
     */
    @Operation(summary = "刷新Token", description = "刷新用户的JWT Token")
    @PostMapping("/refresh-token")
    public Result<Map<String, Object>> refreshToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            if (token == null || token.trim().isEmpty()) {
                return Result.error("Token不能为空");
            }
            
            // 刷新token
            String newToken = jwtUtil.refreshToken(token);
            if (newToken != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("token", newToken);
                return Result.success(data);
            }
            return Result.error("Token刷新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "用户登录", description = "用户登录接口，返回用户信息和Token")
    @RepeatSubmit(interval = 3, message = "登录请求过于频繁，请稍后再试")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        try {
            // 获取客户端IP
            String clientIp = getClientIp(request);
            
            // 登录并更新最后登录信息
            UserVO userVO = userService.login(loginDTO.getUsername(), loginDTO.getPassword(), clientIp);
            
            // 生成JWT token
            String token = jwtUtil.generateToken(loginDTO.getUsername());
            
            Map<String, Object> data = new HashMap<>();
            data.put("user", userVO);
            data.put("token", token);
            
            // 记录登录成功日志
            loginLogService.recordLoginLog(loginDTO.getUsername(), "0", "登录成功");
            
            return Result.success(data);
        } catch (Exception e) {
            // 记录登录失败日志
            loginLogService.recordLoginLog(loginDTO.getUsername(), "1", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取客户端真实IPv4地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }
        
        // 处理IPv6本地地址，转换为IPv4
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }
        
        // 如果是IPv6地址，尝试获取IPv4地址
        if (ip != null && ip.contains(":") && !ip.contains(".")) {
            // 这是一个IPv6地址，在本地开发环境下使用127.0.0.1
            ip = "127.0.0.1";
        }
        
        return ip;
    }

    @Operation(summary = "用户注册", description = "新用户注册接口")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            userService.register(registerDTO);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "查询用户列表", description = "分页查询用户列表")
    @GetMapping("/list")
    public Result<Page<User>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) String studentNo,
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Integer status) {
        
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            queryWrapper.like("username", username);
        }
        if (realName != null && !realName.isEmpty()) {
            queryWrapper.like("real_name", realName);
        }
        if (studentNo != null && !studentNo.isEmpty()) {
            queryWrapper.like("student_no", studentNo);
        }
        if (userType != null && !userType.isEmpty()) {
            queryWrapper.eq("user_type", userType);
        }
        if (roleId != null) {
            queryWrapper.eq("role_id", roleId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("create_time");
        
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> result = userService.getUserListWithRole(page, queryWrapper);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @Log(title = "用户管理", businessType = 1)
    @PostMapping
    public Result<User> save(@RequestBody User user) {
        user.setPassword(SecureUtil.md5(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user);
        return Result.success(user);
    }

    @Log(title = "用户管理", businessType = 2)
    @PutMapping
    public Result<User> update(@RequestBody User user) {
        user.setUpdateTime(LocalDateTime.now());
        userService.updateById(user);
        return Result.success(user);
    }

    @Log(title = "用户管理", businessType = 3)
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }
}
