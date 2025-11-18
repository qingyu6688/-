package com.example.admin.vo;

import com.example.admin.entity.CommunityComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评论VO - 包含用户信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentVO extends CommunityComment {
    
    /**
     * 评论人姓名
     */
    private String userName;
    
    /**
     * 评论人头像
     */
    private String userAvatar;
    
    /**
     * 回复对象的姓名
     */
    private String replyToUserName;
    
    /**
     * 当前用户是否已点赞
     */
    private Boolean isLiked;
}
