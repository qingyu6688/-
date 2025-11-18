package com.example.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.common.Result;
import com.example.admin.entity.*;
import com.example.admin.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动管理模块控制器
 * 包含：活动信息、报名管理、签到管理、评价管理、相册管理
 */
@Slf4j
@RestController
@RequestMapping("/api/activity")
@Tag(name = "活动管理", description = "活动信息、报名、签到、评价等功能")
public class ActivityController {

    @Autowired
    private ActivityInfoService activityInfoService;

    @Autowired
    private ActivityRegistrationService registrationService;

    @Autowired
    private ActivitySignInService signInService;

    @Autowired
    private ActivityEvaluationService evaluationService;

    @Autowired
    private ActivityPhotoService photoService;

    @Autowired
    private UserService userService;

    // ==================== 活动信息管理 ====================

    /**
     * 分页查询活动列表
     */
    @Operation(summary = "分页查询活动列表")
    @GetMapping("/info/list")
    public Result<IPage<ActivityInfo>> getActivityList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String activityName,
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String auditStatus
    ) {
        Page<ActivityInfo> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ActivityInfo> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.like(activityName != null && !activityName.isEmpty(), ActivityInfo::getActivityName, activityName)
               .eq(activityType != null && !activityType.isEmpty(), ActivityInfo::getActivityType, activityType)
               .eq(status != null && !status.isEmpty(), ActivityInfo::getStatus, status)
               .eq(auditStatus != null && !auditStatus.isEmpty(), ActivityInfo::getAuditStatus, auditStatus)
               .orderByDesc(ActivityInfo::getCreateTime);

        IPage<ActivityInfo> result = activityInfoService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 获取活动详情
     */
    @Operation(summary = "获取活动详情")
    @GetMapping("/info/{id}")
    public Result<ActivityInfo> getActivityById(@PathVariable Long id) {
        ActivityInfo activity = activityInfoService.getById(id);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        return Result.success(activity);
    }

    /**
     * 新增活动
     */
    @Operation(summary = "新增活动")
    @PostMapping("/info")
    public Result<String> addActivity(@RequestBody ActivityInfo activity) {
        activity.setCreateTime(LocalDateTime.now());
        activity.setUpdateTime(LocalDateTime.now());
        activity.setCurrentParticipants(0);
        activity.setStatus("0"); // 报名中
        activity.setAuditStatus("0"); // 待审核
        
        boolean success = activityInfoService.save(activity);
        return success ? Result.success("创建成功") : Result.error("创建失败");
    }

    /**
     * 更新活动
     */
    @Operation(summary = "更新活动")
    @PutMapping("/info")
    public Result<String> updateActivity(@RequestBody ActivityInfo activity) {
        activity.setUpdateTime(LocalDateTime.now());
        boolean success = activityInfoService.updateById(activity);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除活动
     */
    @Operation(summary = "删除活动")
    @DeleteMapping("/info/{id}")
    public Result<String> deleteActivity(@PathVariable Long id) {
        boolean success = activityInfoService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 审核活动
     */
    @Operation(summary = "审核活动")
    @PutMapping("/info/{id}/audit")
    public Result<String> auditActivity(
            @PathVariable Long id,
            @RequestParam String auditStatus
    ) {
        ActivityInfo activity = activityInfoService.getById(id);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        activity.setAuditStatus(auditStatus);
        activity.setUpdateTime(LocalDateTime.now());
        boolean success = activityInfoService.updateById(activity);
        return success ? Result.success("审核成功") : Result.error("审核失败");
    }

    /**
     * 取消活动
     */
    @Operation(summary = "取消活动")
    @PutMapping("/info/{id}/cancel")
    public Result<String> cancelActivity(@PathVariable Long id) {
        ActivityInfo activity = activityInfoService.getById(id);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        activity.setStatus("3"); // 已取消
        activity.setUpdateTime(LocalDateTime.now());
        boolean success = activityInfoService.updateById(activity);
        return success ? Result.success("取消成功") : Result.error("取消失败");
    }

    // ==================== 报名管理 ====================

    /**
     * 分页查询报名列表
     */
    @Operation(summary = "分页查询报名列表")
    @GetMapping("/registration/list")
    public Result<IPage<ActivityRegistration>> getRegistrationList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long activityId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String auditStatus
    ) {
        Page<ActivityRegistration> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ActivityRegistration> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(activityId != null, ActivityRegistration::getActivityId, activityId)
               .eq(userId != null, ActivityRegistration::getUserId, userId)
               .eq(auditStatus != null && !auditStatus.isEmpty(), ActivityRegistration::getAuditStatus, auditStatus)
               .orderByDesc(ActivityRegistration::getCreateTime);

        IPage<ActivityRegistration> result = registrationService.page(page, wrapper);
        
        // 关联查询活动信息
        for (ActivityRegistration registration : result.getRecords()) {
            if (registration.getActivityId() != null) {
                ActivityInfo activity = activityInfoService.getById(registration.getActivityId());
                registration.setActivity(activity);
            }
        }
        
        return Result.success(result);
    }

    /**
     * 创建报名
     */
    @Operation(summary = "创建报名")
    @PostMapping("/registration")
    public Result<String> addRegistration(@RequestBody ActivityRegistration registration) {
        // 检查活动是否存在
        ActivityInfo activity = activityInfoService.getById(registration.getActivityId());
        if (activity == null) {
            return Result.error("活动不存在");
        }
        
        // 检查是否已报名
        LambdaQueryWrapper<ActivityRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityRegistration::getActivityId, registration.getActivityId())
               .eq(ActivityRegistration::getUserId, registration.getUserId());
        long count = registrationService.count(wrapper);
        if (count > 0) {
            return Result.error("您已报名该活动");
        }
        
        // 检查活动是否已满员
        if (activity.getMaxParticipants() > 0 && 
            activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            return Result.error("活动已满员");
        }
        
        // 设置报名信息
        registration.setCreateTime(LocalDateTime.now());
        registration.setUpdateTime(LocalDateTime.now());
        
        // 如果活动不需要审核，直接通过
        if (activity.getNeedAudit() == 0) {
            registration.setAuditStatus("1"); // 已通过
            // 更新活动参与人数
            activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
            activityInfoService.updateById(activity);
        } else {
            registration.setAuditStatus("0"); // 待审核
        }
        
        boolean success = registrationService.save(registration);
        return success ? Result.success("报名成功") : Result.error("报名失败");
    }

    /**
     * 审核报名
     */
    @Operation(summary = "审核报名")
    @PutMapping("/registration/{id}/audit")
    public Result<String> auditRegistration(
            @PathVariable Long id,
            @RequestParam String auditStatus
    ) {
        ActivityRegistration registration = registrationService.getById(id);
        if (registration == null) {
            return Result.error("报名记录不存在");
        }
        registration.setAuditStatus(auditStatus);
        registration.setUpdateTime(LocalDateTime.now());
        boolean success = registrationService.updateById(registration);
        
        // 如果审核通过，更新活动参与人数
        if (success && "1".equals(auditStatus)) {
            ActivityInfo activity = activityInfoService.getById(registration.getActivityId());
            if (activity != null) {
                activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
                activityInfoService.updateById(activity);
            }
        }
        
        return success ? Result.success("审核成功") : Result.error("审核失败");
    }

    /**
     * 取消报名
     */
    @Operation(summary = "取消报名")
    @DeleteMapping("/registration/{id}")
    public Result<String> cancelRegistration(@PathVariable Long id) {
        ActivityRegistration registration = registrationService.getById(id);
        if (registration == null) {
            return Result.error("报名记录不存在");
        }
        
        boolean success = registrationService.removeById(id);
        
        // 如果已通过审核，更新活动参与人数
        if (success && "1".equals(registration.getAuditStatus())) {
            ActivityInfo activity = activityInfoService.getById(registration.getActivityId());
            if (activity != null && activity.getCurrentParticipants() > 0) {
                activity.setCurrentParticipants(activity.getCurrentParticipants() - 1);
                activityInfoService.updateById(activity);
            }
        }
        
        return success ? Result.success("取消成功") : Result.error("取消失败");
    }

    // ==================== 签到管理 ====================

    /**
     * 分页查询签到列表
     */
    @Operation(summary = "分页查询签到列表")
    @GetMapping("/signin/list")
    public Result<IPage<ActivitySignIn>> getSignInList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long activityId,
            @RequestParam(required = false) Long userId
    ) {
        Page<ActivitySignIn> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ActivitySignIn> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(activityId != null, ActivitySignIn::getActivityId, activityId)
               .eq(userId != null, ActivitySignIn::getUserId, userId)
               .orderByDesc(ActivitySignIn::getSignInTime);

        IPage<ActivitySignIn> result = signInService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 获取活动签到统计
     */
    @Operation(summary = "获取活动签到统计")
    @GetMapping("/signin/statistics/{activityId}")
    public Result<java.util.Map<String, Object>> getSignInStatistics(@PathVariable Long activityId) {
        LambdaQueryWrapper<ActivitySignIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivitySignIn::getActivityId, activityId);
        
        long totalSignIn = signInService.count(wrapper);
        
        wrapper.eq(ActivitySignIn::getIsLate, 1);
        long lateCount = signInService.count(wrapper);
        
        wrapper.clear();
        wrapper.eq(ActivitySignIn::getActivityId, activityId)
               .eq(ActivitySignIn::getIsEarlyLeave, 1);
        long earlyLeaveCount = signInService.count(wrapper);
        
        java.util.Map<String, Object> statistics = new java.util.HashMap<>();
        statistics.put("totalSignIn", totalSignIn);
        statistics.put("lateCount", lateCount);
        statistics.put("earlyLeaveCount", earlyLeaveCount);
        
        return Result.success(statistics);
    }

    // ==================== 评价管理 ====================

    /**
     * 分页查询评价列表
     */
    @Operation(summary = "分页查询评价列表")
    @GetMapping("/evaluation/list")
    public Result<IPage<ActivityEvaluation>> getEvaluationList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long activityId,
            @RequestParam(required = false) Long userId
    ) {
        Page<ActivityEvaluation> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ActivityEvaluation> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(activityId != null, ActivityEvaluation::getActivityId, activityId)
               .eq(userId != null, ActivityEvaluation::getUserId, userId)
               .orderByDesc(ActivityEvaluation::getCreateTime);

        IPage<ActivityEvaluation> result = evaluationService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 删除评价
     */
    @Operation(summary = "删除评价")
    @DeleteMapping("/evaluation/{id}")
    public Result<String> deleteEvaluation(@PathVariable Long id) {
        boolean success = evaluationService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 获取活动评价统计
     */
    @Operation(summary = "获取活动评价统计")
    @GetMapping("/evaluation/statistics/{activityId}")
    public Result<java.util.Map<String, Object>> getEvaluationStatistics(@PathVariable Long activityId) {
        LambdaQueryWrapper<ActivityEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityEvaluation::getActivityId, activityId);
        
        List<ActivityEvaluation> evaluations = evaluationService.list(wrapper);
        
        if (evaluations.isEmpty()) {
            java.util.Map<String, Object> statistics = new java.util.HashMap<>();
            statistics.put("totalCount", 0);
            statistics.put("averageRating", 0.0);
            return Result.success(statistics);
        }
        
        double totalRating = evaluations.stream()
                .mapToInt(ActivityEvaluation::getRating)
                .sum();
        double averageRating = totalRating / evaluations.size();
        
        java.util.Map<String, Object> statistics = new java.util.HashMap<>();
        statistics.put("totalCount", evaluations.size());
        statistics.put("averageRating", Math.round(averageRating * 10.0) / 10.0);
        
        return Result.success(statistics);
    }

    // ==================== 相册管理 ====================

    /**
     * 分页查询相册列表
     */
    @Operation(summary = "分页查询相册列表")
    @GetMapping("/photo/list")
    public Result<IPage<ActivityPhoto>> getPhotoList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long activityId,
            @RequestParam(required = false) String activityName,
            @RequestParam(required = false) String uploaderName,
            @RequestParam(required = false) Integer isFeatured
    ) {
        Page<ActivityPhoto> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ActivityPhoto> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(activityId != null, ActivityPhoto::getActivityId, activityId)
               .eq(isFeatured != null, ActivityPhoto::getIsFeatured, isFeatured)
               .orderByDesc(ActivityPhoto::getCreateTime);

        IPage<ActivityPhoto> result = photoService.page(page, wrapper);
        
        // 关联查询活动名称和上传者姓名
        for (ActivityPhoto photo : result.getRecords()) {
            // 查询活动名称
            if (photo.getActivityId() != null) {
                ActivityInfo activity = activityInfoService.getById(photo.getActivityId());
                if (activity != null) {
                    photo.setActivityName(activity.getActivityName());
                }
            }
            
            // 查询上传者姓名
            if (photo.getUploaderId() != null) {
                User uploader = userService.getById(photo.getUploaderId());
                if (uploader != null) {
                    photo.setUploaderName(uploader.getNickname());
                }
            }
        }
        
        // 根据活动名称和上传者姓名过滤（前端过滤）
        if (activityName != null && !activityName.isEmpty()) {
            result.getRecords().removeIf(photo -> 
                photo.getActivityName() == null || !photo.getActivityName().contains(activityName)
            );
        }
        if (uploaderName != null && !uploaderName.isEmpty()) {
            result.getRecords().removeIf(photo -> 
                photo.getUploaderName() == null || !photo.getUploaderName().contains(uploaderName)
            );
        }
        
        return Result.success(result);
    }

    /**
     * 删除照片
     */
    @Operation(summary = "删除照片")
    @DeleteMapping("/photo/{id}")
    public Result<String> deletePhoto(@PathVariable Long id) {
        boolean success = photoService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除照片
     */
    @Operation(summary = "批量删除照片")
    @DeleteMapping("/photo/batch")
    public Result<String> deletePhotoBatch(@RequestBody List<Long> ids) {
        boolean success = photoService.removeByIds(ids);
        return success ? Result.success("批量删除成功") : Result.error("批量删除失败");
    }
    
    /**
     * 设置照片精选状态
     */
    @Operation(summary = "设置照片精选状态")
    @PutMapping("/photo/feature/{id}")
    public Result<String> setPhotoFeature(
            @PathVariable Long id,
            @RequestParam Integer isFeatured
    ) {
        ActivityPhoto photo = photoService.getById(id);
        if (photo == null) {
            return Result.error("照片不存在");
        }
        
        photo.setIsFeatured(isFeatured);
        photo.setUpdateTime(LocalDateTime.now());
        boolean success = photoService.updateById(photo);
        
        return success ? Result.success("设置成功") : Result.error("设置失败");
    }
    
    /**
     * 批量上传活动照片
     */
    @Operation(summary = "批量上传活动照片")
    @PostMapping("/photo/upload")
    public Result<List<String>> uploadPhotos(
            @RequestParam Long activityId,
            @RequestParam Long uploaderId,
            @RequestParam("files") List<org.springframework.web.multipart.MultipartFile> files
    ) {
        // 验证活动是否存在
        ActivityInfo activity = activityInfoService.getById(activityId);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        
        // 验证文件数量
        if (files == null || files.isEmpty()) {
            return Result.error("请选择要上传的照片");
        }
        if (files.size() > 50) {
            return Result.error("最多只能上传50张照片");
        }
        
        List<String> photoUrls = new java.util.ArrayList<>();
        
        try {
            for (org.springframework.web.multipart.MultipartFile file : files) {
                // 验证文件类型
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    continue; // 跳过非图片文件
                }
                
                // 上传文件
                String fileUrl = uploadFile(file);
                
                // 保存照片记录
                ActivityPhoto photo = new ActivityPhoto();
                photo.setActivityId(activityId);
                photo.setUploaderId(uploaderId);
                photo.setPhotoUrl(fileUrl);
                photo.setPhotoType("image");
                photo.setLikeCount(0);
                photo.setIsFeatured(0);
                photo.setCreateTime(LocalDateTime.now());
                photo.setUpdateTime(LocalDateTime.now());
                
                photoService.save(photo);
                photoUrls.add(fileUrl);
            }
            
            return Result.success(photoUrls);
        } catch (Exception e) {
            log.error("上传照片失败", e);
            return Result.error("上传照片失败: " + e.getMessage());
        }
    }
    
    /**
     * 上传单个文件的辅助方法
     */
    private String uploadFile(org.springframework.web.multipart.MultipartFile file) throws Exception {
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new Exception("文件名为空");
        }
        
        // 获取文件扩展名
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        
        // 生成新文件名
        String fileName = java.util.UUID.randomUUID().toString().replace("-", "") + extension;
        
        // 按日期创建目录 - 使用绝对路径
        String datePath = java.time.LocalDate.now().toString().replace("-", "/");
        
        // 获取项目根目录
        String projectPath = System.getProperty("user.dir");
        String uploadDir = projectPath + "/uploads/" + datePath;
        
        // 创建目录
        java.io.File dir = new java.io.File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new Exception("创建目录失败: " + uploadDir);
            }
        }
        
        // 保存文件
        String filePath = uploadDir + "/" + fileName;
        java.io.File destFile = new java.io.File(filePath);
        file.transferTo(destFile);
        
        // 返回访问路径（相对路径）
        return "/uploads/" + datePath + "/" + fileName;
    }

    // ==================== 统计信息 ====================

    /**
     * 获取活动统计信息
     */
    @Operation(summary = "获取活动统计信息")
    @GetMapping("/statistics")
    public Result<java.util.Map<String, Object>> getStatistics() {
        long totalActivities = activityInfoService.count();
        
        // 进行中的活动
        LambdaQueryWrapper<ActivityInfo> ongoingWrapper = new LambdaQueryWrapper<>();
        ongoingWrapper.eq(ActivityInfo::getStatus, "1");
        long ongoingActivities = activityInfoService.count(ongoingWrapper);
        
        // 待审核的活动
        LambdaQueryWrapper<ActivityInfo> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(ActivityInfo::getAuditStatus, "0");
        long pendingActivities = activityInfoService.count(pendingWrapper);
        
        // 总报名人数
        long totalRegistrations = registrationService.count();
        
        // 总评价数
        long totalEvaluations = evaluationService.count();
        
        java.util.Map<String, Object> statistics = new java.util.HashMap<>();
        statistics.put("totalActivities", totalActivities);
        statistics.put("ongoingActivities", ongoingActivities);
        statistics.put("pendingActivities", pendingActivities);
        statistics.put("totalRegistrations", totalRegistrations);
        statistics.put("totalEvaluations", totalEvaluations);
        
        return Result.success(statistics);
    }
}
