package com.example.admin.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.annotation.Log;
import com.example.admin.common.Result;
import com.example.admin.dto.UpdatePasswordDTO;
import com.example.admin.dto.UpdateProfileDTO;
import com.example.admin.entity.*;
import com.example.admin.service.*;
import com.example.admin.util.JwtUtil;
import com.example.admin.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private CommunityPostService communityPostService;
    
    @Autowired
    private ActivityRegistrationService activityRegistrationService;
    
    @Autowired
    private LoginLogService loginLogService;
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserVO> getProfile(HttpServletRequest request) {
        try {
            String username = getUsernameFromRequest(request);
            System.out.println("获取到的用户名: " + username);
            
            if (StrUtil.isBlank(username)) {
                return Result.error("无法获取用户信息，请重新登录");
            }
            
            User user = userService.getUserByUsername(username);
            if (user != null) {
                UserVO userVO = userService.getUserVOById(user.getId());
                return Result.success(userVO);
            }
            return Result.error("用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改密码
     */
    @Log(title = "个人中心", businessType = 2)
    @PostMapping("/password")
    public Result<Void> updatePassword(@RequestBody UpdatePasswordDTO dto, HttpServletRequest request) {
        try {
            // 验证参数
            if (StrUtil.isBlank(dto.getOldPassword()) || 
                StrUtil.isBlank(dto.getNewPassword()) || 
                StrUtil.isBlank(dto.getConfirmPassword())) {
                return Result.error("密码不能为空");
            }
            
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                return Result.error("两次输入的密码不一致");
            }
            
            if (dto.getNewPassword().length() < 6) {
                return Result.error("新密码长度不能少于6位");
            }
            
            // 获取当前用户
            String username = getUsernameFromRequest(request);
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 验证旧密码
            String oldPasswordMd5 = SecureUtil.md5(dto.getOldPassword());
            if (!oldPasswordMd5.equals(user.getPassword())) {
                return Result.error("原密码错误");
            }
            
            // 更新密码
            user.setPassword(SecureUtil.md5(dto.getNewPassword()));
            userService.updateById(user);
            
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新个人资料
     */
    @Log(title = "个人中心", businessType = 2)
    @PutMapping("/info")
    public Result<Void> updateProfile(@RequestBody UpdateProfileDTO dto, HttpServletRequest request) {
        try {
            String username = getUsernameFromRequest(request);
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 更新基本信息
            if (StrUtil.isNotBlank(dto.getNickname())) {
                user.setNickname(dto.getNickname());
            }
            if (StrUtil.isNotBlank(dto.getRealName())) {
                user.setRealName(dto.getRealName());
            }
            if (dto.getSex() != null) {
                user.setSex(dto.getSex());
            }
            if (StrUtil.isNotBlank(dto.getEmail())) {
                user.setEmail(dto.getEmail());
            }
            if (StrUtil.isNotBlank(dto.getPhone())) {
                user.setPhone(dto.getPhone());
            }
            if (StrUtil.isNotBlank(dto.getWechat())) {
                user.setWechat(dto.getWechat());
            }
            if (StrUtil.isNotBlank(dto.getQq())) {
                user.setQq(dto.getQq());
            }
            if (StrUtil.isNotBlank(dto.getAvatar())) {
                user.setAvatar(dto.getAvatar());
            }
            
            // 更新学校信息
            if (StrUtil.isNotBlank(dto.getCollege())) {
                user.setCollege(dto.getCollege());
            }
            if (StrUtil.isNotBlank(dto.getMajor())) {
                user.setMajor(dto.getMajor());
            }
            if (StrUtil.isNotBlank(dto.getClassName())) {
                user.setClassName(dto.getClassName());
            }
            if (dto.getEnrollmentYear() != null) {
                user.setEnrollmentYear(dto.getEnrollmentYear());
            }
            
            userService.updateById(user);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取个人操作记录
     */
    @GetMapping("/operations")
    public Result<Page<OperationLog>> getOperations(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            String username = getUsernameFromRequest(request);
            
            QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("operator_name", username);
            queryWrapper.orderByDesc("operator_time");
            
            Page<OperationLog> page = new Page<>(pageNum, pageSize);
            Page<OperationLog> result = operationLogService.page(page, queryWrapper);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户统计数据
     */
    @GetMapping("/stats/{userId}")
    public Result<Map<String, Object>> getUserStats(@PathVariable Long userId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 获取用户信息
            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 统计登录次数（通过username）
            LambdaQueryWrapper<LoginLog> loginWrapper = new LambdaQueryWrapper<>();
            loginWrapper.eq(LoginLog::getUsername, user.getUsername());
            long loginCount = loginLogService.count(loginWrapper);
            stats.put("loginCount", loginCount);
            
            // 统计发帖数
            LambdaQueryWrapper<CommunityPost> postWrapper = new LambdaQueryWrapper<>();
            postWrapper.eq(CommunityPost::getUserId, userId);
            long postCount = communityPostService.count(postWrapper);
            stats.put("postCount", postCount);
            
            // 统计参与活动数
            LambdaQueryWrapper<ActivityRegistration> regWrapper = new LambdaQueryWrapper<>();
            regWrapper.eq(ActivityRegistration::getUserId, userId);
            regWrapper.eq(ActivityRegistration::getAuditStatus, "1"); // 只统计已通过的
            long activityCount = activityRegistrationService.count(regWrapper);
            stats.put("activityCount", activityCount);
            
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 从请求中获取用户名
     */
    private String getUsernameFromRequest(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            System.out.println("=== 开始解析Token ===");
            System.out.println("收到的完整Token: [" + token + "]");
            System.out.println("Token长度: " + (token != null ? token.length() : "null"));
            
            if (StrUtil.isBlank(token)) {
                System.out.println("Token为空或空白");
                return null;
            }
            
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
                System.out.println("去除Bearer后的Token: [" + token + "]");
                System.out.println("去除Bearer后Token长度: " + token.length());
            }
            
            String username = jwtUtil.getUsernameFromToken(token);
            System.out.println("解析出的用户名: " + username);
            System.out.println("=== Token解析结束 ===");
            return username;
        } catch (Exception e) {
            System.err.println("解析token异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
