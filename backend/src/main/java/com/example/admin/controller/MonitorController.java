package com.example.admin.controller;

import com.example.admin.common.Result;
import com.example.admin.util.ServerUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    
    /**
     * 获取服务器信息
     */
    @GetMapping("/server")
    public Result<Map<String, Object>> getServerInfo() {
        try {
            Map<String, Object> serverInfo = ServerUtil.getServerInfo();
            return Result.success(serverInfo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取系统概览信息
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        try {
            Map<String, Object> overview = new HashMap<>();
            Map<String, Object> serverInfo = ServerUtil.getServerInfo();
            
            // 提取关键指标
            @SuppressWarnings("unchecked")
            Map<String, Object> cpu = (Map<String, Object>) serverInfo.get("cpu");
            @SuppressWarnings("unchecked")
            Map<String, Object> memory = (Map<String, Object>) serverInfo.get("memory");
            @SuppressWarnings("unchecked")
            Map<String, Object> disk = (Map<String, Object>) serverInfo.get("disk");
            
            overview.put("cpuUsage", cpu.get("cpuUsage"));
            overview.put("memoryUsage", memory.get("usage"));
            overview.put("diskUsage", disk.get("usage"));
            overview.put("cpuCount", cpu.get("cpuCount"));
            
            return Result.success(overview);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
