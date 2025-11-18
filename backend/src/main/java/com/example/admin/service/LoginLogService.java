package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.LoginLog;

public interface LoginLogService extends IService<LoginLog> {
    
    /**
     * 记录登录日志
     */
    void recordLoginLog(String username, String status, String msg);
}
