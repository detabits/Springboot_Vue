package com.qingge.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Category;
import com.qingge.springboot.entity.Product;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.mapper.ProductMapper;
import com.qingge.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/product")
public class ProductController {

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody Product product) {
        // 新增或者更新
        return productService.saveProduct(product);
    }


    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result findAll() {
        List<Product> list =productService.list();
        return Result.success(list);
    }



    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return productMapper.deleteById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) { // [1,2,3]
        return productService.removeByIds(ids);
    }


        // 分页查询 - mybatis-plus的方式
        @GetMapping("/page")
        public IPage<Product> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String productname,
                                    @RequestParam(defaultValue = "") String productcode,
                                    @RequestParam(defaultValue = "") String productclassification
                                     ) {
            IPage<Product> page = new Page<>(pageNum, pageSize);
            QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            if (!"".equals(productname)) {
                queryWrapper.like("productname", productname);
            }
            if (!"".equals(productcode)) {
                queryWrapper.like("productcode", productcode);
            }
            if (!"".equals(productclassification)) {
                queryWrapper.like("productclassification", productclassification);
            }

            queryWrapper.orderByDesc("id");

            return productService.page(page, queryWrapper);
        }

}