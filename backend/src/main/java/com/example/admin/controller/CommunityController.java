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
 * 互动交流模块控制器
 * 包含：帖子管理、评论管理、板块管理、通知管理
 */
@Slf4j
@RestController
@RequestMapping("/api/community")
@Tag(name = "互动交流管理", description = "社区论坛、评论、通知等功能")
public class CommunityController {

    @Autowired
    private CommunityPostService postService;

    @Autowired
    private CommunityCommentService commentService;

    @Autowired
    private CommunityForumCategoryService categoryService;

    @Autowired
    private CommunityNoticeService noticeService;

    @Autowired
    private CommunityLikeService likeService;

    @Autowired
    private CommunityCollectService collectService;

    // ==================== 帖子管理 ====================

    /**
     * 分页查询帖子列表
     */
    @Operation(summary = "分页查询帖子列表")
    @GetMapping("/post/list")
    public Result<IPage<CommunityPost>> getPostList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String auditStatus,
            @RequestParam(required = false) Integer isTop,
            @RequestParam(required = false) Integer isHot,
            @RequestParam(required = false) Integer isEssence
    ) {
        Page<CommunityPost> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CommunityPost> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.like(title != null && !title.isEmpty(), CommunityPost::getTitle, title)
               .eq(categoryId != null, CommunityPost::getCategoryId, categoryId)
               .eq(userId != null, CommunityPost::getUserId, userId)
               .eq(status != null && !status.isEmpty(), CommunityPost::getStatus, status)
               .eq(auditStatus != null && !auditStatus.isEmpty(), CommunityPost::getAuditStatus, auditStatus)
               .eq(isTop != null, CommunityPost::getIsTop, isTop)
               .eq(isHot != null, CommunityPost::getIsHot, isHot)
               .eq(isEssence != null, CommunityPost::getIsEssence, isEssence)
               .orderByDesc(CommunityPost::getIsTop)
               .orderByDesc(CommunityPost::getCreateTime);

        IPage<CommunityPost> result = postService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 获取帖子详情
     */
    @Operation(summary = "获取帖子详情")
    @GetMapping("/post/{id}")
    public Result<CommunityPost> getPostById(@PathVariable Long id) {
        CommunityPost post = postService.getById(id);
        if (post == null) {
            return Result.error("帖子不存在");
        }
        // 增加浏览次数
        post.setViewCount(post.getViewCount() + 1);
        postService.updateById(post);
        return Result.success(post);
    }

    /**
     * 新增帖子
     */
    @Operation(summary = "新增帖子")
    @PostMapping("/post")
    public Result<String> addPost(@RequestBody CommunityPost post) {
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCollectCount(0);
        post.setIsTop(0);
        post.setIsHot(0);
        post.setIsEssence(0);
        post.setStatus("0"); // 正常
        post.setAuditStatus("0"); // 待审核
        
        boolean success = postService.save(post);
        return success ? Result.success("发布成功") : Result.error("发布失败");
    }

    /**
     * 更新帖子
     */
    @Operation(summary = "更新帖子")
    @PutMapping("/post")
    public Result<String> updatePost(@RequestBody CommunityPost post) {
        post.setUpdateTime(LocalDateTime.now());
        boolean success = postService.updateById(post);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除帖子
     */
    @Operation(summary = "删除帖子")
    @DeleteMapping("/post/{id}")
    public Result<String> deletePost(@PathVariable Long id) {
        boolean success = postService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除帖子
     */
    @Operation(summary = "批量删除帖子")
    @DeleteMapping("/post/batch")
    public Result<String> deletePostBatch(@RequestBody List<Long> ids) {
        boolean success = postService.removeByIds(ids);
        return success ? Result.success("批量删除成功") : Result.error("批量删除失败");
    }

    /**
     * 审核帖子
     */
    @Operation(summary = "审核帖子")
    @PutMapping("/post/{id}/audit")
    public Result<String> auditPost(
            @PathVariable Long id,
            @RequestParam String auditStatus
    ) {
        CommunityPost post = postService.getById(id);
        if (post == null) {
            return Result.error("帖子不存在");
        }
        post.setAuditStatus(auditStatus);
        post.setUpdateTime(LocalDateTime.now());
        boolean success = postService.updateById(post);
        return success ? Result.success("审核成功") : Result.error("审核失败");
    }

    /**
     * 设置帖子置顶
     */
    @Operation(summary = "设置帖子置顶")
    @PutMapping("/post/{id}/top")
    public Result<String> setPostTop(@PathVariable Long id, @RequestParam Integer isTop) {
        CommunityPost post = postService.getById(id);
        if (post == null) {
            return Result.error("帖子不存在");
        }
        post.setIsTop(isTop);
        post.setUpdateTime(LocalDateTime.now());
        boolean success = postService.updateById(post);
        return success ? Result.success("设置成功") : Result.error("设置失败");
    }

    /**
     * 设置帖子精华
     */
    @Operation(summary = "设置帖子精华")
    @PutMapping("/post/{id}/essence")
    public Result<String> setPostEssence(@PathVariable Long id, @RequestParam Integer isEssence) {
        CommunityPost post = postService.getById(id);
        if (post == null) {
            return Result.error("帖子不存在");
        }
        post.setIsEssence(isEssence);
        post.setUpdateTime(LocalDateTime.now());
        boolean success = postService.updateById(post);
        return success ? Result.success("设置成功") : Result.error("设置失败");
    }

    // ==================== 评论管理 ====================

    /**
     * 分页查询评论列表
     */
    @Operation(summary = "分页查询评论列表")
    @GetMapping("/comment/list")
    public Result<IPage<CommunityComment>> getCommentList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status
    ) {
        Page<CommunityComment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CommunityComment> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(postId != null, CommunityComment::getPostId, postId)
               .eq(userId != null, CommunityComment::getUserId, userId)
               .eq(status != null && !status.isEmpty(), CommunityComment::getStatus, status)
               .orderByDesc(CommunityComment::getCreateTime);

        IPage<CommunityComment> result = commentService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 获取帖子的评论列表
     */
    @Operation(summary = "获取帖子的评论列表")
    @GetMapping("/comment/post/{postId}")
    public Result<List<CommunityComment>> getCommentsByPostId(@PathVariable Long postId) {
        LambdaQueryWrapper<CommunityComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityComment::getPostId, postId)
               .eq(CommunityComment::getStatus, "0")
               .orderByDesc(CommunityComment::getCreateTime);
        List<CommunityComment> comments = commentService.list(wrapper);
        return Result.success(comments);
    }

    /**
     * 新增评论
     */
    @Operation(summary = "新增评论")
    @PostMapping("/comment")
    public Result<String> addComment(@RequestBody CommunityComment comment) {
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        comment.setLikeCount(0);
        comment.setStatus("0"); // 正常
        
        boolean success = commentService.save(comment);
        
        if (success) {
            // 更新帖子评论数
            CommunityPost post = postService.getById(comment.getPostId());
            if (post != null) {
                post.setCommentCount(post.getCommentCount() + 1);
                postService.updateById(post);
            }
            return Result.success("评论成功");
        }
        return Result.error("评论失败");
    }

    /**
     * 删除评论
     */
    @Operation(summary = "删除评论")
    @DeleteMapping("/comment/{id}")
    public Result<String> deleteComment(@PathVariable Long id) {
        CommunityComment comment = commentService.getById(id);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        
        boolean success = commentService.removeById(id);
        
        if (success) {
            // 更新帖子评论数
            CommunityPost post = postService.getById(comment.getPostId());
            if (post != null && post.getCommentCount() > 0) {
                post.setCommentCount(post.getCommentCount() - 1);
                postService.updateById(post);
            }
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    // ==================== 板块管理 ====================

    /**
     * 获取所有板块
     */
    @Operation(summary = "获取所有板块")
    @GetMapping("/category/list")
    public Result<List<CommunityForumCategory>> getCategoryList() {
        LambdaQueryWrapper<CommunityForumCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(CommunityForumCategory::getSortOrder);
        List<CommunityForumCategory> categories = categoryService.list(wrapper);
        return Result.success(categories);
    }

    /**
     * 新增板块
     */
    @Operation(summary = "新增板块")
    @PostMapping("/category")
    public Result<String> addCategory(@RequestBody CommunityForumCategory category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setPostCount(0);
        boolean success = categoryService.save(category);
        return success ? Result.success("新增成功") : Result.error("新增失败");
    }

    /**
     * 更新板块
     */
    @Operation(summary = "更新板块")
    @PutMapping("/category")
    public Result<String> updateCategory(@RequestBody CommunityForumCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        boolean success = categoryService.updateById(category);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除板块
     */
    @Operation(summary = "删除板块")
    @DeleteMapping("/category/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        // 检查是否有帖子
        LambdaQueryWrapper<CommunityPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityPost::getCategoryId, id);
        long count = postService.count(wrapper);
        if (count > 0) {
            return Result.error("该板块下还有帖子，无法删除");
        }
        
        boolean success = categoryService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    // ==================== 通知管理 ====================

    /**
     * 分页查询通知列表
     */
    @Operation(summary = "分页查询通知列表")
    @GetMapping("/notice/list")
    public Result<IPage<CommunityNotice>> getNoticeList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String noticeType
    ) {
        Page<CommunityNotice> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CommunityNotice> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.like(title != null && !title.isEmpty(), CommunityNotice::getTitle, title)
               .eq(noticeType != null && !noticeType.isEmpty(), CommunityNotice::getNoticeType, noticeType)
               .orderByDesc(CommunityNotice::getCreateTime);

        IPage<CommunityNotice> result = noticeService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 新增通知
     */
    @Operation(summary = "新增通知")
    @PostMapping("/notice")
    public Result<String> addNotice(@RequestBody CommunityNotice notice) {
        notice.setCreateTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());
        notice.setReadCount(0);
        boolean success = noticeService.save(notice);
        return success ? Result.success("发布成功") : Result.error("发布失败");
    }

    /**
     * 更新通知
     */
    @Operation(summary = "更新通知")
    @PutMapping("/notice")
    public Result<String> updateNotice(@RequestBody CommunityNotice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        boolean success = noticeService.updateById(notice);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除通知
     */
    @Operation(summary = "删除通知")
    @DeleteMapping("/notice/{id}")
    public Result<String> deleteNotice(@PathVariable Long id) {
        boolean success = noticeService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    // ==================== 统计信息 ====================

    /**
     * 获取社区统计信息
     */
    @Operation(summary = "获取社区统计信息")
    @GetMapping("/statistics")
    public Result<java.util.Map<String, Object>> getStatistics() {
        long totalPosts = postService.count();
        long totalComments = commentService.count();
        long totalCategories = categoryService.count();
        
        // 待审核帖子数
        LambdaQueryWrapper<CommunityPost> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(CommunityPost::getAuditStatus, "0");
        long pendingPosts = postService.count(pendingWrapper);
        
        // 今日新增帖子
        LambdaQueryWrapper<CommunityPost> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.ge(CommunityPost::getCreateTime, LocalDateTime.now().toLocalDate().atStartOfDay());
        long todayPosts = postService.count(todayWrapper);
        
        java.util.Map<String, Object> statistics = new java.util.HashMap<>();
        statistics.put("totalPosts", totalPosts);
        statistics.put("totalComments", totalComments);
        statistics.put("totalCategories", totalCategories);
        statistics.put("pendingPosts", pendingPosts);
        statistics.put("todayPosts", todayPosts);
        
        return Result.success(statistics);
    }
}
