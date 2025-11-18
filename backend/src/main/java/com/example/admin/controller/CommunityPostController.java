package com.example.admin.controller;

import com.example.admin.annotation.Log;
import com.example.admin.common.Result;
import com.example.admin.entity.CommunityPost;
import com.example.admin.service.CommunityPostService;
import com.example.admin.service.CommunityLikeService;
import com.example.admin.service.CommunityCollectService;
import com.example.admin.vo.PostVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 帖子表 Controller
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Tag(name = "帖子表管理", description = "帖子表相关接口")
@RestController
@RequestMapping("/api/communityPost")
public class CommunityPostController {

    @Autowired
    private CommunityPostService communityPostService;
    
    @Autowired
    private CommunityLikeService likeService;
    
    @Autowired
    private CommunityCollectService collectService;

    /**
     * 分页查询帖子表列表（包含用户信息）
     */
    @Operation(summary = "查询帖子表列表", description = "分页查询帖子表列表")
    @GetMapping("/list")
    public Result<Page<PostVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String auditStatus,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long currentUserId) {
        
        Map<String, Object> params = new HashMap<>();
        // 支持title和keyword两种参数
        if (title != null && !title.isEmpty()) {
            params.put("keyword", title);
        } else {
            params.put("keyword", keyword);
        }
        params.put("categoryId", categoryId);
        params.put("auditStatus", auditStatus);
        params.put("status", status);
        
        Page<PostVO> page = new Page<>(pageNum, pageSize);
        Page<PostVO> result = communityPostService.getPostListWithUser(page, params, currentUserId);
        return Result.success(result);
    }

    /**
     * 根据ID查询帖子详情（包含用户信息）
     */
    @Operation(summary = "查询帖子表详情", description = "根据ID查询帖子表详情")
    @GetMapping("/{id}")
    public Result<PostVO> getById(@PathVariable Long id, @RequestParam(required = false) Long currentUserId) {
        PostVO postVO = communityPostService.getPostDetailById(id, currentUserId);
        return Result.success(postVO);
    }

    /**
     * 新增帖子表
     */
    @Log(title = "帖子表管理", businessType = 1)
    @Operation(summary = "新增帖子表", description = "新增帖子表")
    @PostMapping
    public Result<CommunityPost> save(@RequestBody CommunityPost communityPost) {
        communityPost.setCreateTime(LocalDateTime.now());
        communityPost.setUpdateTime(LocalDateTime.now());
        
        boolean success = communityPostService.save(communityPost);
        
        // 检查审核状态，如果未通过则提示用户详细原因
        if (success && "2".equals(communityPost.getAuditStatus())) {
            // 获取审核失败的详细原因
            String reason = com.example.admin.service.impl.CommunityPostServiceImpl.getAuditFailReason();
            String message = "您的帖子审核未通过！\n原因：" + (reason != null ? reason : "内容可能包含违规信息") + 
                           "\n\n帖子已保存，但暂时不会显示。您可以修改后重新提交，或等待管理员审核。";
            return Result.error(message);
        }
        
        return Result.success(communityPost);
    }

    /**
     * 更新帖子表
     */
    @Log(title = "帖子表管理", businessType = 2)
    @Operation(summary = "更新帖子表", description = "更新帖子表")
    @PutMapping
    public Result<CommunityPost> update(@RequestBody CommunityPost communityPost) {
        communityPost.setUpdateTime(LocalDateTime.now());
        communityPostService.updateById(communityPost);
        return Result.success(communityPost);
    }

    /**
     * 删除帖子表
     */
    @Log(title = "帖子表管理", businessType = 3)
    @Operation(summary = "删除帖子表", description = "根据ID删除帖子表")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        communityPostService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除帖子表
     */
    @Log(title = "帖子表管理", businessType = 3)
    @Operation(summary = "批量删除帖子表", description = "批量删除帖子表")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody java.util.List<Long> ids) {
        communityPostService.removeByIds(ids);
        return Result.success();
    }
    
    /**
     * 点赞帖子
     */
    @Operation(summary = "点赞帖子", description = "点赞或取消点赞帖子")
    @PostMapping("/{id}/like")
    public Result<Map<String, Object>> likePost(@PathVariable Long id, @RequestParam Long userId) {
        CommunityPost post = communityPostService.getById(id);
        if (post == null) {
            return Result.error("帖子不存在");
        }
        
        // 切换点赞状态
        boolean isLiked = likeService.toggleLike("post", id, userId);
        
        // 从数据库统计真实的点赞数
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.admin.entity.CommunityLike> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(com.example.admin.entity.CommunityLike::getTargetType, "post")
               .eq(com.example.admin.entity.CommunityLike::getTargetId, id);
        long likeCount = likeService.count(wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("isLiked", isLiked);
        result.put("likeCount", likeCount);
        
        return Result.success(result);
    }
    
    /**
     * 收藏帖子
     */
    @Operation(summary = "收藏帖子", description = "收藏或取消收藏帖子")
    @PostMapping("/{id}/collect")
    public Result<Map<String, Object>> collectPost(@PathVariable Long id, @RequestParam Long userId) {
        CommunityPost post = communityPostService.getById(id);
        if (post == null) {
            return Result.error("帖子不存在");
        }
        
        // 切换收藏状态
        boolean isCollected = collectService.toggleCollect("post", id, userId);
        
        // 从数据库统计真实的收藏数
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.admin.entity.CommunityCollect> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(com.example.admin.entity.CommunityCollect::getTargetType, "post")
               .eq(com.example.admin.entity.CommunityCollect::getTargetId, id);
        long collectCount = collectService.count(wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("isCollected", isCollected);
        result.put("collectCount", collectCount);
        
        return Result.success(result);
    }
}
