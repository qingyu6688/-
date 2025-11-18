package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.admin.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    
    /**
     * 根据角色ID查询菜单
     */
    @Select("SELECT DISTINCT m.* FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId} AND m.status = '0' " +
            "ORDER BY m.parent_id, m.order_num")
    List<Menu> selectMenusByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据角色ID查询权限标识
     */
    @Select("SELECT DISTINCT m.perms FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId} AND m.status = '0' AND m.perms IS NOT NULL AND m.perms != ''")
    List<String> selectPermsByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 查询所有菜单（树形结构）
     */
    @Select("SELECT * FROM sys_menu WHERE status = '0' ORDER BY parent_id, order_num")
    List<Menu> selectMenuTree();
}
