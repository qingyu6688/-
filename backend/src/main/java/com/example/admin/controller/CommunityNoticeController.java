package com.example.admin.controller;

import com.example.admin.common.Result;
import com.example.admin.entity.CommunityNotice;
import com.example.admin.service.CommunityNoticeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 通知公告 Controller
 */
@Tag(name = "通知公告", description = "通知公告相关接口")
@RestController
@RequestMapping("/api/communityNotice")
public class CommunityNoticeController {

    @Autowired
    private CommunityNoticeService noticeService;

    /**
     * 分页查询公告列表
     */
    @Operation(summary = "分页查询公告", description = "分页查询公告列表")
    @GetMapping("/page")
    public Result<Page<CommunityNotice>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String noticeType,
            @RequestParam(required = false) String status) {
        Page<CommunityNotice> page = noticeService.getNoticePage(pageNum, pageSize, title, noticeType, status);
        return Result.success(page);
    }

    /**
     * 获取前台公告列表（已发布）
     */
    @Operation(summary = "前台公告列表", description = "获取已发布的公告列表")
    @GetMapping("/list")
    public Result<Page<CommunityNotice>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId) {
        Page<CommunityNotice> page = noticeService.getPublishedNotices(pageNum, pageSize, userId);
        return Result.success(page);
    }

    /**
     * 根据ID查询公告详情
     */
    @Operation(summary = "查询公告详情", description = "根据ID查询公告详情")
    @GetMapping("/{id}")
    public Result<CommunityNotice> getById(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        CommunityNotice notice = noticeService.getById(id);
        if (notice == null) {
            return Result.error("公告不存在");
        }
        
        // 记录阅读
        if (userId != null) {
            noticeService.markAsRead(id, userId);
        }
        
        return Result.success(notice);
    }

    /**
     * 新增公告
     */
    @Operation(summary = "新增公告", description = "新增公告")
    @PostMapping
    public Result<CommunityNotice> add(@RequestBody CommunityNotice notice, @RequestParam(required = false) Long publisherId) {
        notice.setCreateTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());
        notice.setReadCount(0);
        
        if (notice.getStatus() == null) {
            notice.setStatus("0"); // 默认草稿
        }
        
        if (notice.getIsTop() == null) {
            notice.setIsTop(0);
        }
        
        // 设置发布人ID，如果没有传入则使用默认值1（管理员）
        if (publisherId != null) {
            notice.setPublisherId(publisherId);
        } else {
            notice.setPublisherId(1L); // 默认管理员ID
        }
        
        noticeService.save(notice);
        return Result.success(notice);
    }

    /**
     * 更新公告
     */
    @Operation(summary = "更新公告", description = "更新公告")
    @PutMapping
    public Result<Void> update(@RequestBody CommunityNotice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        noticeService.updateById(notice);
        return Result.success();
    }

    /**
     * 删除公告
     */
    @Operation(summary = "删除公告", description = "删除公告")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success();
    }

    /**
     * 发布公告
     */
    @Operation(summary = "发布公告", description = "发布公告")
    @PutMapping("/{id}/publish")
    public Result<Void> publish(@PathVariable Long id) {
        noticeService.publishNotice(id);
        return Result.success();
    }

    /**
     * 撤回公告
     */
    @Operation(summary = "撤回公告", description = "撤回公告")
    @PutMapping("/{id}/withdraw")
    public Result<Void> withdraw(@PathVariable Long id) {
        noticeService.withdrawNotice(id);
        return Result.success();
    }

    /**
     * 置顶/取消置顶
     */
    @Operation(summary = "置顶公告", description = "置顶或取消置顶公告")
    @PutMapping("/{id}/top")
    public Result<Void> toggleTop(@PathVariable Long id, @RequestParam Integer isTop) {
        noticeService.toggleTop(id, isTop);
        return Result.success();
    }

    /**
     * 获取未读公告数
     */
    @Operation(summary = "未读公告数", description = "获取用户未读公告数量")
    @GetMapping("/unread/count")
    public Result<Integer> getUnreadCount(@RequestParam Long userId) {
        int count = noticeService.getUnreadCount(userId);
        return Result.success(count);
    }

    /**
     * 获取公告阅读统计
     */
    @Operation(summary = "阅读统计", description = "获取公告阅读统计")
    @GetMapping("/{id}/statistics")
    public Result<Map<String, Object>> getStatistics(@PathVariable Long id) {
        Map<String, Object> stats = noticeService.getReadStatistics(id);
        return Result.success(stats);
    }

    /**
     * 标记通知为已读
     */
    @Operation(summary = "标记已读", description = "标记通知为已读")
    @PostMapping("/{id}/read")
    public Result<Void> markRead(@PathVariable Long id, @RequestParam Long userId) {
        noticeService.markAsRead(id, userId);
        return Result.success();
    }
}
