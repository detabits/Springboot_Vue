package com.qingge.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Region;
import com.qingge.springboot.mapper.RegionMapper;
import com.qingge.springboot.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/region")
public class RegionController {

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody Region region) {
        // 新增或者更新
        return regionService.saveRegion(region);
    }


    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private RegionService regionService;

    @GetMapping
    public Result findAll() {
        List<Region> list =regionService.list();
        return Result.success(list);
    }


    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return regionMapper.deleteById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) { // [1,2,3]
        return regionService.removeByIds(ids);
    }


        // 分页查询 - mybatis-plus的方式
        @GetMapping("/page")
        public IPage<Region> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String province,
                                    @RequestParam(defaultValue = "") String city,
                                      @RequestParam(defaultValue = "") String county
                                    ) {
            IPage<Region> page = new Page<>(pageNum, pageSize);
            QueryWrapper<Region> queryWrapper = new QueryWrapper<>();

            if (!"".equals(province) || !"".equals(city) || !"".equals(county)) {
                queryWrapper.and(wrapper -> {
                    if (!"".equals(province)) {
                        wrapper.like("province", province);
                    }
                    if (!"".equals(city)) {
                        wrapper.like("city", city);
                    }
                    if (!"".equals(county)) {
                        wrapper.like("county", county);
                    }
                });
            }


            return regionService.page(page, queryWrapper);
        }

}