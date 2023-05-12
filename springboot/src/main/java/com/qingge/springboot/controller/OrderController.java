package com.qingge.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.Order;
import com.qingge.springboot.mapper.OrderMapper;
import com.qingge.springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/order")
public class OrderController {

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody Order order) {
        // 新增或者更新
        return orderService.saveOrder(order);
    }


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;



    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return orderMapper.deleteById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) { // [1,2,3]
        return orderService.removeByIds(ids);
    }


        // 分页查询 - mybatis-plus的方式
        @GetMapping("/page")
        public IPage<Order> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String ordername,
                                    @RequestParam(defaultValue = "") String email,
                                    @RequestParam(defaultValue = "") String address) {
            IPage<Order> page = new Page<>(pageNum, pageSize);
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            if (!"".equals(ordername)) {
                queryWrapper.like("ordername", ordername);
            }
            if (!"".equals(email)) {
                queryWrapper.like("email", email);
            }
            if (!"".equals(address)) {
                queryWrapper.like("address", address);
            }
            queryWrapper.orderByDesc("id");

            return orderService.page(page, queryWrapper);
        }

}