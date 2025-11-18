package com.example.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.common.Result;
import com.example.admin.entity.LoginLog;
import com.example.admin.entity.OperationLog;
import com.example.admin.service.LoginLogService;
import com.example.admin.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/log")
public class LogController {
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private LoginLogService loginLogService;
    
    /**
     * 分页查询操作日志
     */
    @GetMapping("/operation/list")
    public Result<Page<OperationLog>> getOperationLogList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) Integer businessType) {
        
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("operator_time");
        
        if (title != null && !title.isEmpty()) {
            queryWrapper.like("title", title);
        }
        if (operatorName != null && !operatorName.isEmpty()) {
            queryWrapper.like("operator_name", operatorName);
        }
        if (businessType != null) {
            queryWrapper.eq("business_type", businessType);
        }
        
        Page<OperationLog> page = new Page<>(pageNum, pageSize);
        Page<OperationLog> result = operationLogService.page(page, queryWrapper);
        return Result.success(result);
    }
    
    /**
     * 删除操作日志
     */
    @DeleteMapping("/operation/{id}")
    public Result<Void> deleteOperationLog(@PathVariable Long id) {
        try {
            operationLogService.removeById(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 清空操作日志
     */
    @DeleteMapping("/operation/clean")
    public Result<Void> cleanOperationLog() {
        try {
            operationLogService.remove(new QueryWrapper<>());
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询登录日志
     */
    @GetMapping("/login/list")
    public Result<Page<LoginLog>> getLoginLogList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String status) {
        
        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("login_time");
        
        if (username != null && !username.isEmpty()) {
            queryWrapper.like("username", username);
        }
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
        
        Page<LoginLog> page = new Page<>(pageNum, pageSize);
        Page<LoginLog> result = loginLogService.page(page, queryWrapper);
        return Result.success(result);
    }
    
    /**
     * 删除登录日志
     */
    @DeleteMapping("/login/{id}")
    public Result<Void> deleteLoginLog(@PathVariable Long id) {
        try {
            loginLogService.removeById(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 清空登录日志
     */
    @DeleteMapping("/login/clean")
    public Result<Void> cleanLoginLog() {
        try {
            loginLogService.remove(new QueryWrapper<>());
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
