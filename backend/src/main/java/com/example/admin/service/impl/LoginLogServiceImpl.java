package com.example.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.LoginLog;
import com.example.admin.mapper.LoginLogMapper;
import com.example.admin.service.LoginLogService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {
    
    @Override
    public void recordLoginLog(String username, String status, String msg) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }
            
            HttpServletRequest request = attributes.getRequest();
            
            LoginLog loginLog = new LoginLog();
            loginLog.setUsername(username);
            loginLog.setStatus(status);
            loginLog.setMsg(msg);
            loginLog.setLoginTime(LocalDateTime.now());
            
            // 获取IP地址
            String ip = getIpAddr(request);
            loginLog.setIpAddress(ip);
            
            // 解析User-Agent
            String userAgentStr = request.getHeader("User-Agent");
            if (StrUtil.isNotBlank(userAgentStr)) {
                UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
                loginLog.setBrowser(userAgent.getBrowser().getName());
                loginLog.setOs(userAgent.getOs().getName());
            }
            
            // 保存日志
            this.save(loginLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取客户端IP
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
