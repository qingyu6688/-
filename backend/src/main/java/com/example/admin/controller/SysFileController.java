package com.example.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.annotation.Log;
import com.example.admin.common.Result;
import com.example.admin.entity.SysFile;
import com.example.admin.service.FileService;
import com.example.admin.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sysfile")
public class SysFileController {
    
    @Autowired
    private SysFileService sysFileService;
    
    @Autowired
    private FileService fileService;
    
    /**
     * 分页查询文件列表
     */
    @GetMapping("/list")
    public Result<Page<SysFile>> getFileList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) String fileType) {
        
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        if (fileName != null && !fileName.isEmpty()) {
            queryWrapper.like("original_name", fileName);
        }
        if (fileType != null && !fileType.isEmpty()) {
            queryWrapper.eq("file_type", fileType);
        }
        queryWrapper.orderByDesc("upload_time");
        
        Page<SysFile> page = new Page<>(pageNum, pageSize);
        Page<SysFile> result = sysFileService.page(page, queryWrapper);
        return Result.success(result);
    }
    
    /**
     * 获取文件统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> statistics = sysFileService.getFileStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除文件
     */
    @Log(title = "文件管理", businessType = 3)
    @DeleteMapping("/{id}")
    public Result<Void> deleteFile(@PathVariable Long id) {
        try {
            SysFile sysFile = sysFileService.getById(id);
            if (sysFile != null) {
                // 删除物理文件
                fileService.deleteFile(sysFile.getFileUrl());
                // 删除数据库记录
                sysFileService.removeById(id);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 批量删除文件
     */
    @Log(title = "文件管理", businessType = 3)
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        try {
            for (Long id : ids) {
                SysFile sysFile = sysFileService.getById(id);
                if (sysFile != null) {
                    // 删除物理文件
                    fileService.deleteFile(sysFile.getFileUrl());
                }
            }
            // 批量删除数据库记录
            sysFileService.removeByIds(ids);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
