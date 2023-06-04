package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.entity.Collect;
import com.qingge.springboot.mapper.CollectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CollectService extends ServiceImpl<CollectMapper, Collect> {

    @Resource
    private CollectMapper collectMapper;

}
