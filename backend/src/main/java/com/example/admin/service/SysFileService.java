package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.SysFile;

import java.util.Map;

public interface SysFileService extends IService<SysFile> {
    
    /**
     * 获取文件统计信息
     */
    Map<String, Object> getFileStatistics();
}
