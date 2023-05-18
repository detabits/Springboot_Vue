package com.qingge.springboot.controller;

import cn.hutool.db.AbstractDb;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Temp;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.mapper.TempMapper;
import com.qingge.springboot.service.TempService;
import com.qingge.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/temp")
public class TempController {

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody Temp temp) {
        // 新增或者更新
        return tempService.saveTemp(temp);
    }


    @Autowired
    private TempMapper tempMapper;

    @Autowired
    private TempService tempService;



    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return tempMapper.deleteById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) { // [1,2,3]
        return tempService.removeByIds(ids);
    }


        // 分页查询 - mybatis-plus的方式
        @GetMapping("/page")
        public IPage<Temp> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String tempname,
                                    @RequestParam(defaultValue = "") String email,
                                    @RequestParam(defaultValue = "") String address) {
            IPage<Temp> page = new Page<>(pageNum, pageSize);
            QueryWrapper<Temp> queryWrapper = new QueryWrapper<>();
            if (!"".equals(tempname)) {
                queryWrapper.like("tempname", tempname);
            }
            if (!"".equals(email)) {
                queryWrapper.like("email", email);
            }
            if (!"".equals(address)) {
                queryWrapper.like("address", address);
            }
            queryWrapper.orderByDesc("id");

            return tempService.page(page, queryWrapper);
        }

}