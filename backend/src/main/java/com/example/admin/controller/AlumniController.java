package com.example.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.annotation.Log;
import com.example.admin.common.Result;
import com.example.admin.entity.*;
import com.example.admin.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 校友信息管理 Controller
 * 包含校友档案、联系方式、社团经历、荣誉记录、贡献度管理
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Tag(name = "校友信息管理", description = "校友档案、联系方式、社团经历、荣誉记录、贡献度相关接口")
@RestController
@RequestMapping("/api/alumni")
public class AlumniController {

    @Autowired
    private AlumniContactExtendService contactExtendService;
    
    @Autowired
    private AlumniClubHistoryService clubHistoryService;
    
    @Autowired
    private AlumniHonorService honorService;
    
    @Autowired
    private AlumniContributionService contributionService;

    // ==================== 联系方式管理 ====================

    /**
     * 获取校友联系方式扩展信息
     */
    @Operation(summary = "获取校友联系方式", description = "根据用户ID获取校友联系方式扩展信息")
    @GetMapping("/contact/{userId}")
    public Result<AlumniContactExtend> getContactByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        QueryWrapper<AlumniContactExtend> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        AlumniContactExtend contact = contactExtendService.getOne(wrapper);
        return Result.success(contact);
    }

    /**
     * 保存或更新校友联系方式
     */
    @Log(title = "校友联系方式管理", businessType = 1)
    @Operation(summary = "保存校友联系方式", description = "保存或更新校友联系方式扩展信息")
    @PostMapping("/contact")
    public Result<AlumniContactExtend> saveContact(@RequestBody AlumniContactExtend contact) {
        if (contact.getId() == null) {
            contact.setCreateTime(LocalDateTime.now());
        }
        contact.setUpdateTime(LocalDateTime.now());
        contactExtendService.saveOrUpdate(contact);
        return Result.success(contact);
    }

    /**
     * 批量查询校友联系方式（支持隐私级别过滤）
     */
    @Operation(summary = "批量查询校友联系方式", description = "根据隐私级别批量查询校友联系方式")
    @GetMapping("/contact/list")
    public Result<Page<AlumniContactExtend>> listContacts(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer privacyLevel,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String industry) {
        
        QueryWrapper<AlumniContactExtend> wrapper = new QueryWrapper<>();
        if (privacyLevel != null) {
            wrapper.eq("privacy_level", privacyLevel);
        }
        if (company != null && !company.isEmpty()) {
            wrapper.like("company", company);
        }
        if (industry != null && !industry.isEmpty()) {
            wrapper.like("industry", industry);
        }
        wrapper.orderByDesc("update_time");
        
        Page<AlumniContactExtend> page = new Page<>(pageNum, pageSize);
        Page<AlumniContactExtend> result = contactExtendService.page(page, wrapper);
        return Result.success(result);
    }

    // ==================== 社团经历管理 ====================

    /**
     * 获取校友社团经历列表
     */
    @Operation(summary = "获取社团经历列表", description = "根据用户ID获取社团经历列表")
    @GetMapping("/club-history/{userId}")
    public Result<List<AlumniClubHistory>> getClubHistoryByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        QueryWrapper<AlumniClubHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("start_date");
        List<AlumniClubHistory> list = clubHistoryService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 分页查询社团经历
     */
    @Operation(summary = "分页查询社团经历", description = "支持按社团名称、职务查询")
    @GetMapping("/club-history/list")
    public Result<Page<AlumniClubHistory>> listClubHistory(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String clubName,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) Boolean isCurrent) {
        
        QueryWrapper<AlumniClubHistory> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (clubName != null && !clubName.isEmpty()) {
            wrapper.like("club_name", clubName);
        }
        if (position != null && !position.isEmpty()) {
            wrapper.like("position", position);
        }
        if (isCurrent != null) {
            wrapper.eq("is_current", isCurrent ? 1 : 0);
        }
        wrapper.orderByDesc("start_date");
        
        Page<AlumniClubHistory> page = new Page<>(pageNum, pageSize);
        Page<AlumniClubHistory> result = clubHistoryService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 新增社团经历
     */
    @Log(title = "社团经历管理", businessType = 1)
    @Operation(summary = "新增社团经历", description = "新增校友社团经历记录")
    @PostMapping("/club-history")
    public Result<AlumniClubHistory> saveClubHistory(@RequestBody AlumniClubHistory clubHistory) {
        clubHistory.setCreateTime(LocalDateTime.now());
        clubHistory.setUpdateTime(LocalDateTime.now());
        clubHistoryService.save(clubHistory);
        return Result.success(clubHistory);
    }

    /**
     * 更新社团经历
     */
    @Log(title = "社团经历管理", businessType = 2)
    @Operation(summary = "更新社团经历", description = "更新校友社团经历记录")
    @PutMapping("/club-history")
    public Result<AlumniClubHistory> updateClubHistory(@RequestBody AlumniClubHistory clubHistory) {
        clubHistory.setUpdateTime(LocalDateTime.now());
        clubHistoryService.updateById(clubHistory);
        return Result.success(clubHistory);
    }

    /**
     * 删除社团经历
     */
    @Log(title = "社团经历管理", businessType = 3)
    @Operation(summary = "删除社团经历", description = "根据ID删除社团经历记录")
    @DeleteMapping("/club-history/{id}")
    public Result<Void> deleteClubHistory(@PathVariable Long id) {
        clubHistoryService.removeById(id);
        return Result.success();
    }

    // ==================== 荣誉记录管理 ====================

    /**
     * 获取校友荣誉记录列表
     */
    @Operation(summary = "获取荣誉记录列表", description = "根据用户ID获取荣誉记录列表")
    @GetMapping("/honor/{userId}")
    public Result<List<AlumniHonor>> getHonorByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        QueryWrapper<AlumniHonor> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("audit_status", "1"); // 只返回已通过审核的
        wrapper.orderByDesc("award_date");
        List<AlumniHonor> list = honorService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 分页查询荣誉记录
     */
    @Operation(summary = "分页查询荣誉记录", description = "支持按荣誉名称、级别、审核状态查询")
    @GetMapping("/honor/list")
    public Result<Page<AlumniHonor>> listHonor(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String honorName,
            @RequestParam(required = false) String honorLevel,
            @RequestParam(required = false) String auditStatus) {
        
        QueryWrapper<AlumniHonor> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (honorName != null && !honorName.isEmpty()) {
            wrapper.like("honor_name", honorName);
        }
        if (honorLevel != null && !honorLevel.isEmpty()) {
            wrapper.eq("honor_level", honorLevel);
        }
        if (auditStatus != null && !auditStatus.isEmpty()) {
            wrapper.eq("audit_status", auditStatus);
        }
        wrapper.orderByDesc("award_date");
        
        Page<AlumniHonor> page = new Page<>(pageNum, pageSize);
        Page<AlumniHonor> result = honorService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 新增荣誉记录
     */
    @Log(title = "荣誉记录管理", businessType = 1)
    @Operation(summary = "新增荣誉记录", description = "新增校友荣誉记录，默认待审核状态")
    @PostMapping("/honor")
    public Result<AlumniHonor> saveHonor(@RequestBody AlumniHonor honor) {
        honor.setAuditStatus("0"); // 默认待审核
        honor.setCreateTime(LocalDateTime.now());
        honor.setUpdateTime(LocalDateTime.now());
        honorService.save(honor);
        return Result.success(honor);
    }

    /**
     * 更新荣誉记录
     */
    @Log(title = "荣誉记录管理", businessType = 2)
    @Operation(summary = "更新荣誉记录", description = "更新校友荣誉记录")
    @PutMapping("/honor")
    public Result<AlumniHonor> updateHonor(@RequestBody AlumniHonor honor) {
        honor.setUpdateTime(LocalDateTime.now());
        honorService.updateById(honor);
        return Result.success(honor);
    }

    /**
     * 审核荣誉记录
     */
    @Log(title = "荣誉记录管理", businessType = 2)
    @Operation(summary = "审核荣誉记录", description = "审核校友荣誉记录")
    @PutMapping("/honor/{id}/audit")
    public Result<Void> auditHonor(
            @PathVariable Long id,
            @RequestParam String auditStatus,
            @RequestParam(required = false) String auditor) {
        AlumniHonor honor = honorService.getById(id);
        if (honor == null) {
            return Result.error("荣誉记录不存在");
        }
        honor.setAuditStatus(auditStatus);
        honor.setAuditor(auditor);
        honor.setAuditTime(LocalDateTime.now());
        honor.setUpdateTime(LocalDateTime.now());
        honorService.updateById(honor);
        return Result.success();
    }

    /**
     * 删除荣誉记录
     */
    @Log(title = "荣誉记录管理", businessType = 3)
    @Operation(summary = "删除荣誉记录", description = "根据ID删除荣誉记录")
    @DeleteMapping("/honor/{id}")
    public Result<Void> deleteHonor(@PathVariable Long id) {
        honorService.removeById(id);
        return Result.success();
    }

    // ==================== 贡献度管理 ====================

    /**
     * 获取校友贡献度记录
     */
    @Operation(summary = "获取贡献度记录", description = "根据用户ID获取贡献度记录列表")
    @GetMapping("/contribution/{userId}")
    public Result<List<AlumniContribution>> getContributionByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        QueryWrapper<AlumniContribution> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        List<AlumniContribution> list = contributionService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 获取校友总贡献值
     */
    @Operation(summary = "获取总贡献值", description = "计算用户的总贡献值")
    @GetMapping("/contribution/{userId}/total")
    public Result<Integer> getTotalContribution(@PathVariable Long userId) {
        QueryWrapper<AlumniContribution> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.select("IFNULL(SUM(contribution_value), 0) as total");
        List<AlumniContribution> list = contributionService.list(wrapper);
        Integer total = list.isEmpty() ? 0 : list.get(0).getContributionValue();
        return Result.success(total);
    }

    /**
     * 分页查询贡献度记录
     */
    @Operation(summary = "分页查询贡献度记录", description = "支持按用户、贡献类型查询")
    @GetMapping("/contribution/list")
    public Result<Page<AlumniContribution>> listContribution(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String contributionType) {
        
        QueryWrapper<AlumniContribution> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (contributionType != null && !contributionType.isEmpty()) {
            wrapper.eq("contribution_type", contributionType);
        }
        wrapper.orderByDesc("create_time");
        
        Page<AlumniContribution> page = new Page<>(pageNum, pageSize);
        Page<AlumniContribution> result = contributionService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 新增贡献度记录
     */
    @Log(title = "贡献度管理", businessType = 1)
    @Operation(summary = "新增贡献度记录", description = "新增校友贡献度记录")
    @PostMapping("/contribution")
    public Result<AlumniContribution> saveContribution(@RequestBody AlumniContribution contribution) {
        contribution.setCreateTime(LocalDateTime.now());
        contributionService.save(contribution);
        return Result.success(contribution);
    }

    /**
     * 批量删除贡献度记录
     */
    @Log(title = "贡献度管理", businessType = 3)
    @Operation(summary = "批量删除贡献度记录", description = "批量删除贡献度记录")
    @DeleteMapping("/contribution/batch")
    public Result<Void> deleteContributionBatch(@RequestBody List<Long> ids) {
        contributionService.removeByIds(ids);
        return Result.success();
    }

    // ==================== 统计分析 ====================

    /**
     * 获取校友统计信息
     */
    @Operation(summary = "获取校友统计信息", description = "获取指定用户的综合统计信息")
    @GetMapping("/statistics/{userId}")
    public Result<java.util.Map<String, Object>> getStatistics(@PathVariable Long userId) {
        java.util.Map<String, Object> statistics = new java.util.HashMap<>();
        
        // 社团经历数量
        QueryWrapper<AlumniClubHistory> clubWrapper = new QueryWrapper<>();
        clubWrapper.eq("user_id", userId);
        long clubCount = clubHistoryService.count(clubWrapper);
        statistics.put("clubHistoryCount", clubCount);
        
        // 荣誉记录数量（已通过审核）
        QueryWrapper<AlumniHonor> honorWrapper = new QueryWrapper<>();
        honorWrapper.eq("user_id", userId);
        honorWrapper.eq("audit_status", "1");
        long honorCount = honorService.count(honorWrapper);
        statistics.put("honorCount", honorCount);
        
        // 总贡献值
        QueryWrapper<AlumniContribution> contributionWrapper = new QueryWrapper<>();
        contributionWrapper.eq("user_id", userId);
        contributionWrapper.select("IFNULL(SUM(contribution_value), 0) as total");
        List<AlumniContribution> contributions = contributionService.list(contributionWrapper);
        Integer totalContribution = contributions.isEmpty() ? 0 : contributions.get(0).getContributionValue();
        statistics.put("totalContribution", totalContribution);
        
        // 是否填写联系方式
        QueryWrapper<AlumniContactExtend> contactWrapper = new QueryWrapper<>();
        contactWrapper.eq("user_id", userId);
        boolean hasContact = contactExtendService.count(contactWrapper) > 0;
        statistics.put("hasContactInfo", hasContact);
        
        return Result.success(statistics);
    }
}
