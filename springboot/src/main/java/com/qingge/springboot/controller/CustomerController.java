package com.qingge.springboot.controller;

import cn.hutool.db.AbstractDb;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Customer;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.mapper.CustomerMapper;
import com.qingge.springboot.service.CustomerService;
import com.qingge.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/customer")
public class CustomerController {

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody Customer customer) {
        // 新增或者更新
        return customerService.saveCustomer(customer);
    }


    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;



    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return customerMapper.deleteById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) { // [1,2,3]
        return customerService.removeByIds(ids);
    }


        // 分页查询 - mybatis-plus的方式
        @GetMapping("/page")
        public IPage<Customer> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String customername,
                                    @RequestParam(defaultValue = "") String email,
                                    @RequestParam(defaultValue = "") String address) {
            IPage<Customer> page = new Page<>(pageNum, pageSize);
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
            if (!"".equals(customername)) {
                queryWrapper.like("customername", customername);
            }
            if (!"".equals(email)) {
                queryWrapper.like("email", email);
            }
            if (!"".equals(address)) {
                queryWrapper.like("address", address);
            }
            queryWrapper.orderByDesc("id");

            return customerService.page(page, queryWrapper);
        }

}