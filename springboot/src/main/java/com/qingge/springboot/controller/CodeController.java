package com.qingge.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Code;
import com.qingge.springboot.entity.Product;
import com.qingge.springboot.mapper.CodeMapper;
import com.qingge.springboot.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/code")
public class CodeController {

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody Code code) {
        // 新增或者更新
        return codeService.saveCode(code);
    }


    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private CodeService codeService;

    @GetMapping
    public Result findAll() {
        List<Code> list =codeService.list();
        return Result.success(list);
    }


    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return codeMapper.deleteById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) { // [1,2,3]
        return codeService.removeByIds(ids);
    }


        // 分页查询 - mybatis-plus的方式
        @GetMapping("/page")
        public IPage<Code> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String codename,
                                    @RequestParam(defaultValue = "") String itemname
                                    ) {
            IPage<Code> page = new Page<>(pageNum, pageSize);
            QueryWrapper<Code> queryWrapper = new QueryWrapper<>();

            if (!"".equals(codename)) {
                queryWrapper.like("codename", codename);
            }
            if (!"".equals(itemname)) {
                queryWrapper.like("itemname", itemname);
            }



            return codeService.page(page, queryWrapper);
        }

}