package com.qingge.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.Productclassification;
import com.qingge.springboot.mapper.ProductclassificationMapper;
import com.qingge.springboot.service.ProductclassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/productclassification")
public class ProductclassificationController {

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody Productclassification productclassification) {
        // 新增或者更新
        return productclassificationService.saveProductclassification(productclassification);
    }


    @Autowired
    private ProductclassificationMapper productclassificationMapper;

    @Autowired
    private ProductclassificationService productclassificationService;



    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return productclassificationMapper.deleteById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) { // [1,2,3]
        return productclassificationService.removeByIds(ids);
    }


        // 分页查询 - mybatis-plus的方式
        @GetMapping("/page")
        public IPage<Productclassification> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String productclassificationname,
                                    @RequestParam(defaultValue = "") String email,
                                    @RequestParam(defaultValue = "") String address) {
            IPage<Productclassification> page = new Page<>(pageNum, pageSize);
            QueryWrapper<Productclassification> queryWrapper = new QueryWrapper<>();
            if (!"".equals(productclassificationname)) {
                queryWrapper.like("productclassificationname", productclassificationname);
            }
            if (!"".equals(email)) {
                queryWrapper.like("email", email);
            }
            if (!"".equals(address)) {
                queryWrapper.like("address", address);
            }
            queryWrapper.orderByDesc("id");

            return productclassificationService.page(page, queryWrapper);
        }

}