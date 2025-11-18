package com.example.admin.service.impl;

import com.example.admin.entity.CommunityLike;
import com.example.admin.entity.CommunityPost;
import com.example.admin.entity.CommunityComment;
import com.example.admin.mapper.CommunityLikeMapper;
import com.example.admin.mapper.CommunityPostMapper;
import com.example.admin.mapper.CommunityCommentMapper;
import com.example.admin.service.CommunityLikeService;
import com.example.admin.service.CommunityMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 点赞记录表 Service实现类
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Service
public class CommunityLikeServiceImpl extends ServiceImpl<CommunityLikeMapper, CommunityLike> implements CommunityLikeService {

    @Autowired
    private CommunityMessageService messageService;
    
    @Autowired
    private CommunityPostMapper postMapper;
    
    @Autowired
    private CommunityCommentMapper commentMapper;

    @Override
    @Transactional
    public boolean toggleLike(String targetType, Long targetId, Long userId) {
        // 查询是否已点赞
        LambdaQueryWrapper<CommunityLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityLike::getTargetType, targetType)
               .eq(CommunityLike::getTargetId, targetId)
               .eq(CommunityLike::getUserId, userId);
        
        CommunityLike existingLike = this.getOne(wrapper);
        
        if (existingLike != null) {
            // 已点赞，取消点赞
            this.removeById(existingLike.getId());
            return false;
        } else {
            // 未点赞，添加点赞记录
            CommunityLike like = new CommunityLike();
            like.setTargetType(targetType);
            like.setTargetId(targetId);
            like.setUserId(userId);
            like.setCreateTime(LocalDateTime.now());
            like.setUpdateTime(LocalDateTime.now());
            this.save(like);
            
            // 创建点赞通知
            Long receiverId = null;
            if ("post".equals(targetType)) {
                CommunityPost post = postMapper.selectById(targetId);
                if (post != null) {
                    receiverId = post.getUserId();
                }
            } else if ("comment".equals(targetType)) {
                CommunityComment comment = commentMapper.selectById(targetId);
                if (comment != null) {
                    receiverId = comment.getUserId();
                }
            }
            
            if (receiverId != null) {
                messageService.createLikeMessage(userId, receiverId, targetId, targetType);
            }
            
            return true;
        }
    }
    
    @Override
    public boolean isLiked(String targetType, Long targetId, Long userId) {
        LambdaQueryWrapper<CommunityLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityLike::getTargetType, targetType)
               .eq(CommunityLike::getTargetId, targetId)
               .eq(CommunityLike::getUserId, userId);
        
        return this.count(wrapper) > 0;
    }
}
