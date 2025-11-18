package com.example.admin.controller;

import com.example.admin.annotation.Log;
import com.example.admin.common.Result;
import com.example.admin.entity.CommunityComment;
import com.example.admin.entity.CommunityPost;
import com.example.admin.entity.User;
import com.example.admin.mapper.UserMapper;
import com.example.admin.service.CommunityCommentService;
import com.example.admin.service.CommunityPostService;
import com.example.admin.service.CommunityLikeService;
import com.example.admin.vo.CommentVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表 Controller
 */
@Tag(name = "评论管理", description = "评论相关接口")
@RestController
@RequestMapping("/api/communityComment")
public class CommunityCommentController {

    @Autowired
    private CommunityCommentService commentService;
    
    @Autowired
    private CommunityPostService postService;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private CommunityLikeService likeService;

    /**
     * 分页查询评论列表（后台管理）
     */
    @Operation(summary = "查询评论列表", description = "分页查询评论列表")
    @GetMapping("/list")
    public Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page<CommentVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String content) {
        
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<CommunityComment> page = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<CommunityComment> wrapper = new LambdaQueryWrapper<>();
        if (postId != null) {
            wrapper.eq(CommunityComment::getPostId, postId);
        }
        if (userId != null) {
            wrapper.eq(CommunityComment::getUserId, userId);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(CommunityComment::getStatus, status);
        }
        if (content != null && !content.isEmpty()) {
            wrapper.like(CommunityComment::getContent, content);
        }
        wrapper.orderByDesc(CommunityComment::getCreateTime);
        
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<CommunityComment> result = commentService.page(page, wrapper);
        
        // 转换为CommentVO并填充用户信息
        List<CommentVO> commentVOList = result.getRecords().stream().map(comment -> {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            
            // 查询评论人信息
            if (comment.getUserId() != null) {
                User user = userMapper.selectById(comment.getUserId());
                if (user != null) {
                    commentVO.setUserName(user.getNickname() != null ? user.getNickname() : user.getUsername());
                    commentVO.setUserAvatar(user.getAvatar());
                }
            }
            
            return commentVO;
        }).collect(Collectors.toList());
        
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<CommentVO> voPage = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(commentVOList);
        
        return Result.success(voPage);
    }

    /**
     * 根据帖子ID查询评论列表
     */
    @Operation(summary = "查询帖子评论", description = "根据帖子ID查询评论列表")
    @GetMapping("/post/{postId}")
    public Result<List<CommentVO>> getCommentsByPostId(@PathVariable Long postId, @RequestParam(required = false) Long currentUserId) {
        LambdaQueryWrapper<CommunityComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityComment::getPostId, postId)
               .eq(CommunityComment::getStatus, "0") // 只查询正常状态的评论
               .orderByDesc(CommunityComment::getCreateTime);
        
        List<CommunityComment> comments = commentService.list(wrapper);
        
        // 转换为CommentVO并填充用户信息
        List<CommentVO> commentVOList = comments.stream().map(comment -> {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            
            // 查询评论人信息
            if (comment.getUserId() != null) {
                User user = userMapper.selectById(comment.getUserId());
                if (user != null) {
                    commentVO.setUserName(user.getNickname() != null ? user.getNickname() : user.getUsername());
                    commentVO.setUserAvatar(user.getAvatar());
                }
            }
            
            // 查询回复对象的用户信息
            if (comment.getReplyToUserId() != null) {
                User replyToUser = userMapper.selectById(comment.getReplyToUserId());
                if (replyToUser != null) {
                    commentVO.setReplyToUserName(replyToUser.getNickname() != null ? 
                                                 replyToUser.getNickname() : replyToUser.getUsername());
                }
            }
            
            // 查询当前用户是否已点赞
            if (currentUserId != null) {
                commentVO.setIsLiked(likeService.isLiked("comment", comment.getId(), currentUserId));
            }
            
            // 从数据库统计真实的点赞数
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.admin.entity.CommunityLike> likeWrapper = 
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            likeWrapper.eq(com.example.admin.entity.CommunityLike::getTargetType, "comment")
                      .eq(com.example.admin.entity.CommunityLike::getTargetId, comment.getId());
            long realLikeCount = likeService.count(likeWrapper);
            commentVO.setLikeCount((int) realLikeCount);
            
            return commentVO;
        }).collect(Collectors.toList());
        
        return Result.success(commentVOList);
    }

    /**
     * 新增评论
     */
    @Log(title = "评论管理", businessType = 1)
    @Operation(summary = "发表评论", description = "发表新评论")
    @PostMapping
    public Result<CommunityComment> save(@RequestBody CommunityComment comment) {
        try {
            comment.setCreateTime(LocalDateTime.now());
            comment.setUpdateTime(LocalDateTime.now());
            commentService.save(comment);
            
            // 更新帖子的评论数
            CommunityPost post = postService.getById(comment.getPostId());
            if (post != null) {
                post.setCommentCount((post.getCommentCount() != null ? post.getCommentCount() : 0) + 1);
                postService.updateById(post);
            }
            
            return Result.success(comment);
        } catch (RuntimeException e) {
            // 捕获审核不通过的异常
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除评论
     */
    @Log(title = "评论管理", businessType = 3)
    @Operation(summary = "删除评论", description = "根据ID删除评论")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        CommunityComment comment = commentService.getById(id);
        if (comment != null) {
            commentService.removeById(id);
            
            // 更新帖子的评论数
            CommunityPost post = postService.getById(comment.getPostId());
            if (post != null && post.getCommentCount() != null && post.getCommentCount() > 0) {
                post.setCommentCount(post.getCommentCount() - 1);
                postService.updateById(post);
            }
        }
        return Result.success();
    }
    
    /**
     * 点赞评论
     */
    @Operation(summary = "点赞评论", description = "点赞或取消点赞评论")
    @PostMapping("/{id}/like")
    public Result<java.util.Map<String, Object>> likeComment(@PathVariable Long id, @RequestParam Long userId) {
        CommunityComment comment = commentService.getById(id);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        
        // 切换点赞状态
        boolean isLiked = likeService.toggleLike("comment", id, userId);
        
        // 从数据库统计真实的点赞数
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.admin.entity.CommunityLike> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(com.example.admin.entity.CommunityLike::getTargetType, "comment")
               .eq(com.example.admin.entity.CommunityLike::getTargetId, id);
        long likeCount = likeService.count(wrapper);
        
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("isLiked", isLiked);
        result.put("likeCount", likeCount);
        
        return Result.success(result);
    }
}
