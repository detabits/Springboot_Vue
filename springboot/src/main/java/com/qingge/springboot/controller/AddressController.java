package com.qingge.springboot.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Address;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.service.AddressService;
import com.qingge.springboot.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Resource
    private AddressService addressService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        username=userService.findusername(username);  //特事特办
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @PostMapping
    public Result save(@RequestBody Address address) {
        addressService.save(address);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Address address) {
        addressService.updateById(address);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        addressService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        return Result.success(addressService.getById(id));
    }

    @GetMapping
    public Result findAll() {
        LambdaQueryWrapper<Address> query = Wrappers.<Address>lambdaQuery().orderByDesc(Address::getId);
        query.eq(Address::getUserId, getUser().getId());
        List<Address> list = addressService.list(query);
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result findPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String link_user) {
        LambdaQueryWrapper<Address> query = Wrappers.<Address>lambdaQuery().orderByDesc(Address::getId);
//        query.eq(Address::getUserId, getUser().getId());
        if (!"".equals(link_user)) {
            query.like(Address::getLinkUser, link_user);
        }
        IPage<Address> page = addressService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

    @GetMapping("/page/front")
    public Result Front(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        User user = getUser();
        if(user == null) {
            return Result.success(new Page<>());
        }
        LambdaQueryWrapper<Address> query = Wrappers.<Address>lambdaQuery().orderByDesc(Address::getId);
        query.eq(Address::getUserId, getUser().getId());
        IPage<Address> page = addressService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

}
