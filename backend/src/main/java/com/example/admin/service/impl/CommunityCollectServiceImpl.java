package com.example.admin.service.impl;

import com.example.admin.entity.CommunityCollect;
import com.example.admin.entity.CommunityPost;
import com.example.admin.mapper.CommunityCollectMapper;
import com.example.admin.mapper.CommunityPostMapper;
import com.example.admin.service.CommunityCollectService;
import com.example.admin.service.CommunityMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 收藏记录表 Service实现类
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Service
public class CommunityCollectServiceImpl extends ServiceImpl<CommunityCollectMapper, CommunityCollect> implements CommunityCollectService {

    @Autowired
    private CommunityMessageService messageService;
    
    @Autowired
    private CommunityPostMapper postMapper;

    @Override
    @Transactional
    public boolean toggleCollect(String targetType, Long targetId, Long userId) {
        // 查询是否已收藏
        LambdaQueryWrapper<CommunityCollect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityCollect::getTargetType, targetType)
               .eq(CommunityCollect::getTargetId, targetId)
               .eq(CommunityCollect::getUserId, userId);
        
        CommunityCollect existingCollect = this.getOne(wrapper);
        
        if (existingCollect != null) {
            // 已收藏，取消收藏
            this.removeById(existingCollect.getId());
            return false;
        } else {
            // 未收藏，添加收藏记录
            CommunityCollect collect = new CommunityCollect();
            collect.setTargetType(targetType);
            collect.setTargetId(targetId);
            collect.setUserId(userId);
            collect.setCreateTime(LocalDateTime.now());
            collect.setUpdateTime(LocalDateTime.now());
            this.save(collect);
            
            // 创建收藏通知（仅针对帖子）
            if ("post".equals(targetType)) {
                CommunityPost post = postMapper.selectById(targetId);
                if (post != null) {
                    messageService.createCollectMessage(userId, post.getUserId(), targetId);
                }
            }
            
            return true;
        }
    }
    
    @Override
    public boolean isCollected(String targetType, Long targetId, Long userId) {
        LambdaQueryWrapper<CommunityCollect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityCollect::getTargetType, targetType)
               .eq(CommunityCollect::getTargetId, targetId)
               .eq(CommunityCollect::getUserId, userId);
        
        return this.count(wrapper) > 0;
    }
}
