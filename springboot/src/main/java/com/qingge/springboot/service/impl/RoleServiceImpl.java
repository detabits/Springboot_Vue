package com.qingge.springboot.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.qingge.springboot.entity.Role;

import com.qingge.springboot.mapper.RoleMapper;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {

    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return null;
    }
}