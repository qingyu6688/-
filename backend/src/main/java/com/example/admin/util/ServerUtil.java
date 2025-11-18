package com.example.admin.util;

import cn.hutool.core.util.NumberUtil;
import com.sun.management.OperatingSystemMXBean;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 服务器监控工具类
 */
public class ServerUtil {
    
    // 缓存 OperatingSystemMXBean 实例
    private static final OperatingSystemMXBean osmxb = 
        (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    
    /**
     * 获取服务器信息
     */
    public static Map<String, Object> getServerInfo() {
        Map<String, Object> serverInfo = new HashMap<>();
        
        // CPU信息
        serverInfo.put("cpu", getCpuInfo());
        
        // 内存信息
        serverInfo.put("memory", getMemoryInfo());
        
        // JVM信息
        serverInfo.put("jvm", getJvmInfo());
        
        // 磁盘信息
        serverInfo.put("disk", getDiskInfo());
        
        // 系统信息
        serverInfo.put("system", getSystemInfo());
        
        return serverInfo;
    }
    
    /**
     * 获取CPU信息（动态实时获取）
     */
    private static Map<String, Object> getCpuInfo() {
        Map<String, Object> cpuInfo = new HashMap<>();
        
        // CPU核心数
        int cpuCount = osmxb.getAvailableProcessors();
        cpuInfo.put("cpuCount", cpuCount);
        
        // 系统负载（Linux/Unix系统）
        double systemLoad = osmxb.getSystemLoadAverage();
        cpuInfo.put("systemLoad", systemLoad > 0 ? NumberUtil.round(systemLoad, 2).doubleValue() : 0);
        
        // CPU使用率 - 动态采样获取
        double cpuUsage = getDynamicCpuUsage(cpuCount, systemLoad);
        cpuInfo.put("cpuUsage", cpuUsage);
        
        return cpuInfo;
    }
    
    /**
     * 动态获取CPU使用率（多次采样取平均值）
     */
    private static double getDynamicCpuUsage(int cpuCount, double systemLoad) {
        double cpuUsage = 0;
        int sampleCount = 3; // 采样次数
        double totalCpuLoad = 0;
        
        try {
            // 多次采样获取更准确的CPU使用率
            for (int i = 0; i < sampleCount; i++) {
                double systemCpuLoad = osmxb.getSystemCpuLoad();
                
                if (systemCpuLoad >= 0) {
                    // systemCpuLoad 返回 0.0 到 1.0 之间的值
                    totalCpuLoad += systemCpuLoad;
                } else if (systemLoad > 0) {
                    // Linux/Unix 系统使用负载计算
                    totalCpuLoad += Math.min(systemLoad / cpuCount, 1.0);
                } else {
                    // 使用进程CPU负载
                    double processCpuLoad = osmxb.getProcessCpuLoad();
                    if (processCpuLoad >= 0) {
                        totalCpuLoad += processCpuLoad;
                    }
                }
                
                // 短暂休眠以获取不同时刻的采样
                if (i < sampleCount - 1) {
                    Thread.sleep(100);
                }
            }
            
            // 计算平均值
            double avgCpuLoad = totalCpuLoad / sampleCount;
            cpuUsage = NumberUtil.round(avgCpuLoad * 100, 2).doubleValue();
            
        } catch (Exception e) {
            // 如果获取失败，返回默认值
            cpuUsage = 0;
        }
        
        return Math.min(cpuUsage, 100);
    }
    
    /**
     * 获取内存信息（动态实时获取系统物理内存）
     */
    private static Map<String, Object> getMemoryInfo() {
        Map<String, Object> memoryInfo = new HashMap<>();
        
        try {
            // 获取系统物理内存（更真实的内存使用情况）
            long totalPhysicalMemory = osmxb.getTotalPhysicalMemorySize();
            long freePhysicalMemory = osmxb.getFreePhysicalMemorySize();
            long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
            
            memoryInfo.put("total", formatBytes(totalPhysicalMemory));
            memoryInfo.put("used", formatBytes(usedPhysicalMemory));
            memoryInfo.put("free", formatBytes(freePhysicalMemory));
            memoryInfo.put("usage", totalPhysicalMemory > 0 ? 
                NumberUtil.round((double) usedPhysicalMemory / totalPhysicalMemory * 100, 2).doubleValue() : 0);
            
            // 额外添加JVM堆内存信息
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            memoryInfo.put("jvmHeapUsed", formatBytes(heapMemoryUsage.getUsed()));
            memoryInfo.put("jvmHeapMax", formatBytes(heapMemoryUsage.getMax()));
            
        } catch (Exception e) {
            // 降级使用JVM堆内存
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            
            long totalMemory = heapMemoryUsage.getMax();
            long usedMemory = heapMemoryUsage.getUsed();
            long freeMemory = totalMemory - usedMemory;
            
            memoryInfo.put("total", formatBytes(totalMemory));
            memoryInfo.put("used", formatBytes(usedMemory));
            memoryInfo.put("free", formatBytes(freeMemory));
            memoryInfo.put("usage", totalMemory > 0 ? 
                NumberUtil.round((double) usedMemory / totalMemory * 100, 2).doubleValue() : 0);
        }
        
        return memoryInfo;
    }
    
    /**
     * 获取JVM信息
     */
    private static Map<String, Object> getJvmInfo() {
        Map<String, Object> jvmInfo = new HashMap<>();
        
        Properties props = System.getProperties();
        
        jvmInfo.put("name", props.getProperty("java.vm.name"));
        jvmInfo.put("version", props.getProperty("java.version"));
        jvmInfo.put("vendor", props.getProperty("java.vm.vendor"));
        jvmInfo.put("home", props.getProperty("java.home"));
        
        // 运行时长
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        long uptime = System.currentTimeMillis() - startTime;
        jvmInfo.put("uptime", formatUptime(uptime));
        
        return jvmInfo;
    }
    
    /**
     * 获取磁盘信息（动态实时获取所有磁盘）
     */
    private static Map<String, Object> getDiskInfo() {
        Map<String, Object> diskInfo = new HashMap<>();
        
        try {
            File[] roots = File.listRoots();
            long totalSpace = 0;
            long freeSpace = 0;
            long usableSpace = 0;
            
            // 遍历所有磁盘分区，实时获取空间信息
            for (File root : roots) {
                // 每次调用都会重新读取磁盘信息，确保数据是最新的
                long rootTotal = root.getTotalSpace();
                long rootFree = root.getFreeSpace();
                long rootUsable = root.getUsableSpace();
                
                // 只统计有效的磁盘（总空间大于0）
                if (rootTotal > 0) {
                    totalSpace += rootTotal;
                    freeSpace += rootFree;
                    usableSpace += rootUsable;
                }
            }
            
            long usedSpace = totalSpace - freeSpace;
            
            diskInfo.put("total", formatBytes(totalSpace));
            diskInfo.put("used", formatBytes(usedSpace));
            diskInfo.put("free", formatBytes(freeSpace));
            diskInfo.put("usable", formatBytes(usableSpace));
            diskInfo.put("usage", totalSpace > 0 ? 
                NumberUtil.round((double) usedSpace / totalSpace * 100, 2).doubleValue() : 0);
            
        } catch (Exception e) {
            diskInfo.put("total", "0 GB");
            diskInfo.put("used", "0 GB");
            diskInfo.put("free", "0 GB");
            diskInfo.put("usage", 0);
        }
        
        return diskInfo;
    }
    
    /**
     * 获取系统信息
     */
    private static Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        Properties props = System.getProperties();
        
        systemInfo.put("osName", props.getProperty("os.name"));
        systemInfo.put("osArch", props.getProperty("os.arch"));
        systemInfo.put("osVersion", props.getProperty("os.version"));
        systemInfo.put("userName", props.getProperty("user.name"));
        systemInfo.put("userDir", props.getProperty("user.dir"));
        
        return systemInfo;
    }
    
    /**
     * 格式化字节大小
     */
    private static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return NumberUtil.round((double) bytes / 1024, 2) + " KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            return NumberUtil.round((double) bytes / (1024 * 1024), 2) + " MB";
        } else {
            return NumberUtil.round((double) bytes / (1024 * 1024 * 1024), 2) + " GB";
        }
    }
    
    /**
     * 格式化运行时长
     */
    private static String formatUptime(long uptime) {
        long days = uptime / (1000 * 60 * 60 * 24);
        long hours = (uptime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (uptime % (1000 * 60 * 60)) / (1000 * 60);
        
        if (days > 0) {
            return days + "天" + hours + "小时" + minutes + "分钟";
        } else if (hours > 0) {
            return hours + "小时" + minutes + "分钟";
        } else {
            return minutes + "分钟";
        }
    }
}
