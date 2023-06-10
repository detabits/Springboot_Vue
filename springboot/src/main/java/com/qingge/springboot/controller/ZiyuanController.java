package com.qingge.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Ziyuan;
import com.qingge.springboot.mapper.ZiyuanMapper;
import com.qingge.springboot.service.ZiyuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ziyuan")
public class ZiyuanController {

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody Ziyuan ziyuan) {
        // 新增或者更新
        return ziyuanService.saveZiyuan(ziyuan);
    }


    @Autowired
    private ZiyuanMapper ziyuanMapper;

    @Autowired
    private ZiyuanService ziyuanService;

    @GetMapping
    public Result findAll() {
        List<Ziyuan> list =ziyuanService.list();
        return Result.success(list);
    }


    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return ziyuanMapper.deleteById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) { // [1,2,3]
        return ziyuanService.removeByIds(ids);
    }


        // 分页查询 - mybatis-plus的方式
        @GetMapping("/page")
        public IPage<Ziyuan> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String ziyuanname,
                                    @RequestParam(defaultValue = "") String ziyuantype
                                    ) {
            IPage<Ziyuan> page = new Page<>(pageNum, pageSize);
            QueryWrapper<Ziyuan> queryWrapper = new QueryWrapper<>();

            if (!"".equals(ziyuanname)) {
                queryWrapper.like("ziyuanname", ziyuanname);
            }
            if (!"".equals(ziyuantype)) {
                queryWrapper.like("ziyuantype", ziyuantype);
            }

            return ziyuanService.page(page, queryWrapper);
        }

}