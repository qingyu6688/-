package com.example.admin.vo;

import com.example.admin.entity.CommunityPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子VO - 包含用户信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostVO extends CommunityPost {
    
    /**
     * 作者姓名
     */
    private String authorName;
    
    /**
     * 作者头像
     */
    private String authorAvatar;
    
    /**
     * 板块名称
     */
    private String categoryName;
    
    /**
     * 当前用户是否已点赞
     */
    private Boolean isLiked;
    
    /**
     * 当前用户是否已收藏
     */
    private Boolean isCollected;
}
