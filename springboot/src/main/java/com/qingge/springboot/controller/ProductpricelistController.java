package com.qingge.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Product;
import com.qingge.springboot.entity.Productpricelist;
import com.qingge.springboot.mapper.ProductpricelistMapper;
import com.qingge.springboot.service.ProductpricelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/productpricelist")
public class ProductpricelistController {

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody Productpricelist productpricelist) {
        // 新增或者更新
        return productpricelistService.saveProductpricelist(productpricelist);
    }


    @Autowired
    private ProductpricelistMapper productpricelistMapper;

    @Autowired
    private ProductpricelistService productpricelistService;

    @GetMapping
    public Result findAll() {
        List< Productpricelist> list = productpricelistService.list();
        return Result.success(list);
    }



    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return productpricelistMapper.deleteById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) { // [1,2,3]
        return productpricelistService.removeByIds(ids);
    }


        // 分页查询 - mybatis-plus的方式
        @GetMapping("/page")
        public IPage<Productpricelist> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String pricelistname,
                                    @RequestParam(defaultValue = "") String pricelisttype) {
            IPage<Productpricelist> page = new Page<>(pageNum, pageSize);
            QueryWrapper<Productpricelist> queryWrapper = new QueryWrapper<>();
            if (!"".equals(pricelistname)) {
                queryWrapper.like("pricelistname", pricelistname);
            }
            if (!"".equals(pricelisttype)) {
                queryWrapper.like("pricelisttype", pricelisttype);
            }
           
            queryWrapper.orderByDesc("id");

            return productpricelistService.page(page, queryWrapper);
        }

}