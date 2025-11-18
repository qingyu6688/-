package com.example.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.dto.RegisterDTO;
import com.example.admin.entity.Role;
import com.example.admin.entity.User;
import com.example.admin.mapper.RoleMapper;
import com.example.admin.mapper.UserMapper;
import com.example.admin.service.UserService;
import com.example.admin.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserVO login(String username, String password, String clientIp) {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new RuntimeException("用户名或密码不能为空");
        }
        
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = this.getOne(queryWrapper);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        String md5Password = SecureUtil.md5(password);
        if (!md5Password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // 更新最后登录时间和IP
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(clientIp);
        this.updateById(user);
        
        return getUserVOById(user.getId());
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", registerDTO.getUsername());
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (StrUtil.isNotBlank(registerDTO.getEmail())) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", registerDTO.getEmail());
            if (this.count(queryWrapper) > 0) {
                throw new RuntimeException("邮箱已被注册");
            }
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(SecureUtil.md5(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setRoleId(3L); // 默认为普通用户角色(user)
        user.setUserType("student"); // 默认为在校生
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        this.save(user);
    }

    @Override
    public UserVO getUserVOById(Long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        
        // 获取角色信息
        if (user.getRoleId() != null) {
            Role role = roleMapper.selectById(user.getRoleId());
            if (role != null) {
                userVO.setRoleKey(role.getRoleKey());
                userVO.setRoleName(role.getRoleName());
            }
        }
        
        return userVO;
    }
    
    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return this.getOne(queryWrapper);
    }
    
    @Override
    public Page<User> getUserListWithRole(Page<User> page, QueryWrapper<User> queryWrapper) {
        // 查询用户列表
        Page<User> userPage = this.page(page, queryWrapper);
        List<User> userList = userPage.getRecords();
        
        // 为每个用户填充角色信息
        for (User user : userList) {
            if (user.getRoleId() != null) {
                Role role = roleMapper.selectById(user.getRoleId());
                if (role != null) {
                    user.setRoleName(role.getRoleName());
                    user.setRoleKey(role.getRoleKey());
                }
            }
        }
        
        return userPage;
    }
}
