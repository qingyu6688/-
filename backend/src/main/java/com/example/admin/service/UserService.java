package com.example.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.dto.RegisterDTO;
import com.example.admin.entity.User;
import com.example.admin.vo.UserVO;

public interface UserService extends IService<User> {
    UserVO login(String username, String password, String clientIp);
    
    void register(RegisterDTO registerDTO);
    
    UserVO getUserVOById(Long id);
    
    User getUserByUsername(String username);
    
    Page<User> getUserListWithRole(Page<User> page, QueryWrapper<User> queryWrapper);
}
