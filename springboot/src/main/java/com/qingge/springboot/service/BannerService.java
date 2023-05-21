package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.entity.Banner;
import com.qingge.springboot.mapper.BannerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BannerService extends ServiceImpl<BannerMapper, Banner> {

    @Resource
    private BannerMapper bannerMapper;

}
