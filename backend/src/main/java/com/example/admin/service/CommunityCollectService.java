package com.example.admin.service;

import com.example.admin.entity.CommunityCollect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 收藏记录表 Service接口
 * @author CodeGenerator
 * @date 2025-11-17
 */
public interface CommunityCollectService extends IService<CommunityCollect> {

    /**
     * 切换收藏状态（收藏/取消收藏）
     * @param targetType 目标类型（post/activity）
     * @param targetId 目标ID
     * @param userId 用户ID
     * @return true-已收藏，false-已取消
     */
    boolean toggleCollect(String targetType, Long targetId, Long userId);
    
    /**
     * 检查是否已收藏
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @param userId 用户ID
     * @return true-已收藏，false-未收藏
     */
    boolean isCollected(String targetType, Long targetId, Long userId);
}
