package com.example.admin.service.impl;

import com.example.admin.entity.CommunityPost;
import com.example.admin.entity.User;
import com.example.admin.entity.CommunityForumCategory;
import com.example.admin.mapper.CommunityPostMapper;
import com.example.admin.mapper.UserMapper;
import com.example.admin.mapper.CommunityForumCategoryMapper;
import com.example.admin.service.CommunityPostService;
import com.example.admin.utils.ContentAuditUtil;
import com.example.admin.vo.PostVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 帖子表 Service实现类
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Service
public class CommunityPostServiceImpl extends ServiceImpl<CommunityPostMapper, CommunityPost> implements CommunityPostService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private CommunityForumCategoryMapper categoryMapper;
    
    @Autowired
    private com.example.admin.service.CommunityLikeService likeService;
    
    @Autowired
    private com.example.admin.service.CommunityCollectService collectService;
    
    // 使用ThreadLocal存储审核失败原因
    private static final ThreadLocal<String> auditFailReasonHolder = new ThreadLocal<>();

    /**
     * 分页查询帖子列表（包含用户信息）
     */
    @Override
    public Page<PostVO> getPostListWithUser(Page<PostVO> page, Map<String, Object> params, Long currentUserId) {
        // 查询帖子列表
        LambdaQueryWrapper<CommunityPost> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索
        String keyword = (String) params.get("keyword");
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(CommunityPost::getTitle, keyword)
                             .or().like(CommunityPost::getContent, keyword));
        }
        
        // 板块筛选
        Long categoryId = params.get("categoryId") != null ? 
                         Long.valueOf(params.get("categoryId").toString()) : null;
        if (categoryId != null) {
            wrapper.eq(CommunityPost::getCategoryId, categoryId);
        }
        
        // 审核状态筛选
        String auditStatus = (String) params.get("auditStatus");
        if (StringUtils.hasText(auditStatus)) {
            wrapper.eq(CommunityPost::getAuditStatus, auditStatus);
        }
        
        // 状态筛选
        String status = (String) params.get("status");
        if (StringUtils.hasText(status)) {
            wrapper.eq(CommunityPost::getStatus, status);
        }
        
        wrapper.orderByDesc(CommunityPost::getCreateTime);
        
        // 分页查询
        Page<CommunityPost> postPage = new Page<>(page.getCurrent(), page.getSize());
        Page<CommunityPost> result = this.page(postPage, wrapper);
        
        // 转换为PostVO并填充用户信息
        List<PostVO> postVOList = result.getRecords().stream().map(post -> {
            PostVO postVO = new PostVO();
            BeanUtils.copyProperties(post, postVO);
            
            // 查询用户信息
            if (post.getUserId() != null) {
                User user = userMapper.selectById(post.getUserId());
                if (user != null) {
                    postVO.setAuthorName(user.getNickname() != null ? user.getNickname() : user.getUsername());
                    postVO.setAuthorAvatar(user.getAvatar());
                }
            }
            
            // 查询板块信息
            if (post.getCategoryId() != null) {
                CommunityForumCategory category = categoryMapper.selectById(post.getCategoryId());
                if (category != null) {
                    postVO.setCategoryName(category.getCategoryName());
                }
            }
            
            // 查询当前用户是否已点赞和收藏
            if (currentUserId != null) {
                postVO.setIsLiked(likeService.isLiked("post", post.getId(), currentUserId));
                postVO.setIsCollected(collectService.isCollected("post", post.getId(), currentUserId));
            }
            
            // 从数据库统计真实的点赞数和收藏数
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.admin.entity.CommunityLike> likeWrapper = 
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            likeWrapper.eq(com.example.admin.entity.CommunityLike::getTargetType, "post")
                      .eq(com.example.admin.entity.CommunityLike::getTargetId, post.getId());
            long realLikeCount = likeService.count(likeWrapper);
            postVO.setLikeCount((int) realLikeCount);
            
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.admin.entity.CommunityCollect> collectWrapper = 
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            collectWrapper.eq(com.example.admin.entity.CommunityCollect::getTargetType, "post")
                         .eq(com.example.admin.entity.CommunityCollect::getTargetId, post.getId());
            long realCollectCount = collectService.count(collectWrapper);
            postVO.setCollectCount((int) realCollectCount);
            
            return postVO;
        }).collect(Collectors.toList());
        
        // 构建返回的Page对象
        Page<PostVO> voPage = new Page<>(page.getCurrent(), page.getSize(), result.getTotal());
        voPage.setRecords(postVOList);
        
        return voPage;
    }
    
    /**
     * 根据ID查询帖子详情（包含用户信息）
     */
    @Override
    public PostVO getPostDetailById(Long id, Long currentUserId) {
        CommunityPost post = this.getById(id);
        if (post == null) {
            return null;
        }
        
        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post, postVO);
        
        // 查询用户信息
        if (post.getUserId() != null) {
            User user = userMapper.selectById(post.getUserId());
            if (user != null) {
                postVO.setAuthorName(user.getNickname() != null ? user.getNickname() : user.getUsername());
                postVO.setAuthorAvatar(user.getAvatar());
            }
        }
        
        // 查询板块信息
        if (post.getCategoryId() != null) {
            CommunityForumCategory category = categoryMapper.selectById(post.getCategoryId());
            if (category != null) {
                postVO.setCategoryName(category.getCategoryName());
            }
        }
        
        // 查询当前用户是否已点赞和收藏
        if (currentUserId != null) {
            postVO.setIsLiked(likeService.isLiked("post", id, currentUserId));
            postVO.setIsCollected(collectService.isCollected("post", id, currentUserId));
        }
        
        // 从数据库统计真实的点赞数和收藏数
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.admin.entity.CommunityLike> likeWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        likeWrapper.eq(com.example.admin.entity.CommunityLike::getTargetType, "post")
                  .eq(com.example.admin.entity.CommunityLike::getTargetId, id);
        long realLikeCount = likeService.count(likeWrapper);
        postVO.setLikeCount((int) realLikeCount);
        
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.admin.entity.CommunityCollect> collectWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        collectWrapper.eq(com.example.admin.entity.CommunityCollect::getTargetType, "post")
                     .eq(com.example.admin.entity.CommunityCollect::getTargetId, id);
        long realCollectCount = collectService.count(collectWrapper);
        postVO.setCollectCount((int) realCollectCount);
        
        // 增加浏览量
        post.setViewCount((post.getViewCount() != null ? post.getViewCount() : 0) + 1);
        this.updateById(post);
        postVO.setViewCount(post.getViewCount());
        
        return postVO;
    }

    /**
     * 保存帖子（带自动审核）
     */
    @Override
    public boolean save(CommunityPost entity) {
        try {
            // 清空之前的审核原因
            auditFailReasonHolder.remove();
            
            // 自动审核内容
            String auditContent = (entity.getTitle() != null ? entity.getTitle() : "") + 
                                 (entity.getContent() != null ? entity.getContent() : "");
            
            boolean isPass = ContentAuditUtil.auditContent(auditContent);
            
            if (isPass) {
                // 审核通过，自动设置为已通过状态
                entity.setAuditStatus("1"); // 1-已通过
                System.out.println("✅ 帖子自动审核通过: " + entity.getTitle());
            } else {
                // 审核不通过，设置为未通过状态
                entity.setAuditStatus("2"); // 2-未通过（不合规）
                String reason = ContentAuditUtil.getAuditFailReason(auditContent);
                
                // 存储审核失败原因到ThreadLocal
                auditFailReasonHolder.set(reason);
                
                System.out.println("❌ 帖子自动审核未通过，原因: " + reason);
                System.out.println("   帖子标题: " + entity.getTitle());
                // 不合规的帖子仍然保存，但标记为未通过，管理员可以查看
            }
        
        // 设置默认值
        if (entity.getStatus() == null) {
            entity.setStatus("0"); // 0-正常
        }
        if (entity.getIsTop() == null) {
            entity.setIsTop(0);
        }
        if (entity.getIsHot() == null) {
            entity.setIsHot(0);
        }
        if (entity.getIsEssence() == null) {
            entity.setIsEssence(0);
        }
        if (entity.getViewCount() == null) {
            entity.setViewCount(0);
        }
        if (entity.getLikeCount() == null) {
            entity.setLikeCount(0);
        }
        if (entity.getCommentCount() == null) {
            entity.setCommentCount(0);
        }
        if (entity.getCollectCount() == null) {
            entity.setCollectCount(0);
        }
        
        return super.save(entity);
        } finally {
            // 确保清理ThreadLocal，防止内存泄漏
            // 注意：不在这里清理，而是在Controller中获取后清理
        }
    }
    
    /**
     * 获取审核失败原因
     */
    public static String getAuditFailReason() {
        String reason = auditFailReasonHolder.get();
        auditFailReasonHolder.remove(); // 获取后立即清理
        return reason;
    }
}
