package com.example.admin.service;

import com.example.admin.entity.CommunityMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 消息表 Service接口
 * @author CodeGenerator
 * @date 2025-11-17
 */
public interface CommunityMessageService extends IService<CommunityMessage> {

    /**
     * 创建点赞通知
     * @param senderId 点赞人ID
     * @param receiverId 接收人ID
     * @param relatedId 帖子/评论ID
     * @param relatedType 类型(post/comment)
     */
    void createLikeMessage(Long senderId, Long receiverId, Long relatedId, String relatedType);
    
    /**
     * 创建收藏通知
     * @param senderId 收藏人ID
     * @param receiverId 接收人ID
     * @param postId 帖子ID
     */
    void createCollectMessage(Long senderId, Long receiverId, Long postId);
    
    /**
     * 创建评论通知
     * @param senderId 评论人ID
     * @param receiverId 接收人ID
     * @param postId 帖子ID
     * @param commentId 评论ID
     */
    void createCommentMessage(Long senderId, Long receiverId, Long postId, Long commentId);
    
    /**
     * 获取未读消息数量
     * @param userId 用户ID
     * @return 未读数量
     */
    int getUnreadCount(Long userId);
}
