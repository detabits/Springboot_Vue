package com.qingge.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.entity.Goods;
import com.qingge.springboot.entity.Order;
import com.qingge.springboot.mapper.OrderMapper;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Resource
    private OrderMapper orderMapper;



}
