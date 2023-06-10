package com.qingge.springboot.service;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.entity.Menu;
import com.qingge.springboot.entity.Code;
import com.qingge.springboot.mapper.RoleMapper;
import com.qingge.springboot.mapper.RoleMenuMapper;
import com.qingge.springboot.mapper.CodeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodeService extends ServiceImpl<CodeMapper, Code>{
    public boolean saveCode(Code code)
    {
        return saveOrUpdate(code);
    }

    private static final Log LOG = Log.get();

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;


    //获取当前角色的菜单列表
    private List<Menu> getRoleMenus(String roleFlag){
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        //当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        //查出系统所有的菜单
        List<Menu> menus = menuService.findMenus("");
        //new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        //筛选当前用户角色的菜单
        for(Menu menu : menus){
            if(menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            //removeIf() 移除children里面不在menuIds集合中的元素
            children.removeIf(child->!menuIds.contains(child.getId()) );
        }
        return roleMenus;
    }

}


