package com.example.admin.service.impl;

import com.example.admin.entity.CommunityMessage;
import com.example.admin.mapper.CommunityMessageMapper;
import com.example.admin.service.CommunityMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 消息表 Service实现类
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Service
public class CommunityMessageServiceImpl extends ServiceImpl<CommunityMessageMapper, CommunityMessage> implements CommunityMessageService {

    @Override
    public void createLikeMessage(Long senderId, Long receiverId, Long relatedId, String relatedType) {
        // 不给自己发消息
        if (senderId.equals(receiverId)) {
            return;
        }
        
        CommunityMessage message = new CommunityMessage();
        message.setMessageType("notice");
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setRelatedId(relatedId);
        message.setContent("赞了你的" + ("post".equals(relatedType) ? "帖子" : "评论"));
        message.setContentType("text");
        message.setIsRead(0);
        message.setIsWithdraw(0);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        
        this.save(message);
    }
    
    @Override
    public void createCollectMessage(Long senderId, Long receiverId, Long postId) {
        // 不给自己发消息
        if (senderId.equals(receiverId)) {
            return;
        }
        
        CommunityMessage message = new CommunityMessage();
        message.setMessageType("notice");
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setRelatedId(postId);
        message.setContent("收藏了你的帖子");
        message.setContentType("text");
        message.setIsRead(0);
        message.setIsWithdraw(0);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        
        this.save(message);
    }
    
    @Override
    public void createCommentMessage(Long senderId, Long receiverId, Long postId, Long commentId) {
        // 不给自己发消息
        if (senderId.equals(receiverId)) {
            return;
        }
        
        CommunityMessage message = new CommunityMessage();
        message.setMessageType("notice");
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setRelatedId(commentId);
        message.setContent("评论了你的帖子");
        message.setContentType("text");
        message.setIsRead(0);
        message.setIsWithdraw(0);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        
        this.save(message);
    }
    
    @Override
    public int getUnreadCount(Long userId) {
        LambdaQueryWrapper<CommunityMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityMessage::getReceiverId, userId)
               .eq(CommunityMessage::getIsRead, 0);
        return (int) this.count(wrapper);
    }
}
