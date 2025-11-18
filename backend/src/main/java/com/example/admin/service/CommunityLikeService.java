package com.example.admin.service;

import com.example.admin.entity.CommunityLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 点赞记录表 Service接口
 * @author CodeGenerator
 * @date 2025-11-17
 */
public interface CommunityLikeService extends IService<CommunityLike> {

    /**
     * 切换点赞状态（点赞/取消点赞）
     * @param targetType 目标类型（post/comment）
     * @param targetId 目标ID
     * @param userId 用户ID
     * @return true-已点赞，false-已取消
     */
    boolean toggleLike(String targetType, Long targetId, Long userId);
    
    /**
     * 检查是否已点赞
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @param userId 用户ID
     * @return true-已点赞，false-未点赞
     */
    boolean isLiked(String targetType, Long targetId, Long userId);
}
