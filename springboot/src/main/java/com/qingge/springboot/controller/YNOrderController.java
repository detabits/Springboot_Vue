package com.qingge.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.YNOrder;
import com.qingge.springboot.mapper.YNOrderMapper;
import com.qingge.springboot.service.YNOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/order")
public class YNOrderController {

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody YNOrder order) {
        // 新增或者更新
        return orderService.saveOrder(order);
    }


    @Autowired
    private YNOrderMapper orderMapper;

    @Autowired
    private YNOrderService orderService;



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
        public IPage<YNOrder> findPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam(defaultValue = "") String ordername,
                                       @RequestParam(defaultValue = "") String email,
                                       @RequestParam(defaultValue = "") String address) {
            IPage<YNOrder> page = new Page<>(pageNum, pageSize);
            QueryWrapper<YNOrder> queryWrapper = new QueryWrapper<>();
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