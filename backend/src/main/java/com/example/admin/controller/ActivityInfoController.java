package com.example.admin.controller;

import com.example.admin.annotation.Log;
import com.example.admin.common.Result;
import com.example.admin.entity.ActivityInfo;
import com.example.admin.service.ActivityInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 活动信息表 Controller
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Tag(name = "活动信息表管理", description = "活动信息表相关接口")
@RestController
@RequestMapping("/api/activityInfo")
public class ActivityInfoController {

    @Autowired
    private ActivityInfoService activityInfoService;

    /**
     * 分页查询活动信息表列表
     */
    @Operation(summary = "查询活动信息表列表", description = "分页查询活动信息表列表")
    @GetMapping("/list")
    public Result<Page<ActivityInfo>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        
        QueryWrapper<ActivityInfo> queryWrapper = new QueryWrapper<>();
        // TODO: 根据实际需求添加查询条件
        queryWrapper.orderByDesc("create_time");
        
        Page<ActivityInfo> page = new Page<>(pageNum, pageSize);
        Page<ActivityInfo> result = activityInfoService.page(page, queryWrapper);
        return Result.success(result);
    }

    /**
     * 根据ID查询活动信息表
     */
    @Operation(summary = "查询活动信息表详情", description = "根据ID查询活动信息表详情")
    @GetMapping("/{id}")
    public Result<ActivityInfo> getById(@PathVariable Long id) {
        ActivityInfo activityInfo = activityInfoService.getById(id);
        return Result.success(activityInfo);
    }

    /**
     * 新增活动信息表
     */
    @Log(title = "活动信息表管理", businessType = 1)
    @Operation(summary = "新增活动信息表", description = "新增活动信息表")
    @PostMapping
    public Result<ActivityInfo> save(@RequestBody ActivityInfo activityInfo) {
        activityInfo.setCreateTime(LocalDateTime.now());
        activityInfo.setUpdateTime(LocalDateTime.now());
        activityInfoService.save(activityInfo);
        return Result.success(activityInfo);
    }

    /**
     * 更新活动信息表
     */
    @Log(title = "活动信息表管理", businessType = 2)
    @Operation(summary = "更新活动信息表", description = "更新活动信息表")
    @PutMapping
    public Result<ActivityInfo> update(@RequestBody ActivityInfo activityInfo) {
        activityInfo.setUpdateTime(LocalDateTime.now());
        activityInfoService.updateById(activityInfo);
        return Result.success(activityInfo);
    }

    /**
     * 删除活动信息表
     */
    @Log(title = "活动信息表管理", businessType = 3)
    @Operation(summary = "删除活动信息表", description = "根据ID删除活动信息表")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        activityInfoService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除活动信息表
     */
    @Log(title = "活动信息表管理", businessType = 3)
    @Operation(summary = "批量删除活动信息表", description = "批量删除活动信息表")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody java.util.List<Long> ids) {
        activityInfoService.removeByIds(ids);
        return Result.success();
    }
}
