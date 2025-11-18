package com.example.admin.service;

import com.example.admin.entity.CommunityPost;
import com.example.admin.vo.PostVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 帖子表 Service接口
 * @author CodeGenerator
 * @date 2025-11-17
 */
public interface CommunityPostService extends IService<CommunityPost> {

    /**
     * 分页查询帖子列表（包含用户信息）
     */
    Page<PostVO> getPostListWithUser(Page<PostVO> page, Map<String, Object> params, Long currentUserId);
    
    /**
     * 根据ID查询帖子详情（包含用户信息）
     */
    PostVO getPostDetailById(Long id, Long currentUserId);
}
