package com.qingge.springboot.controller;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Goods;

import com.qingge.springboot.entity.Product;
import com.qingge.springboot.entity.Productpricelist;

import com.qingge.springboot.entity.User;
import com.qingge.springboot.entity.YNOrder;
import com.qingge.springboot.service.GoodsService;

import com.qingge.springboot.service.ProductService;
import com.qingge.springboot.service.ProductpricelistService;

import com.qingge.springboot.service.UserService;
import com.qingge.springboot.service.YNOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;


    @Resource
    private ProductService productService;
    @Resource
    private ProductpricelistService productpricelistService;


    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        username=userService.findusername(username);  //特事特办
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @PostMapping
    public Result save(@RequestBody Goods goods) {
        goods.setCreateTime(DateUtil.now());

        Productpricelist productpricelist;
        productpricelist=productpricelistService.getById(goods.getPricelistname());
        if(productpricelist!=null)
        {
            goods.setPricelistname(productpricelist.getPricelistname());  //价目表名称
            goods.setDiscount(productpricelist.getDiscount()); //折扣
        }
        Product product;
        product=productService.getById(goods.getName());
        String gg7;
        gg7=goods.getName();
        if(product!=null){
            goods.setName(product.getProductname());  //产品名称
            goods.setNo(product.getProductcode());  //产品编号
            goods.setStore(product.getStore());  //库存
            goods.setPrice(product.getPrice());  //原价
        }
        goodsService.save(goods);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Goods goods) {

        Productpricelist productpricelist;
        productpricelist=productpricelistService.getById(goods.getPricelistname());
        if(productpricelist!=null)
        {
            goods.setPricelistname(productpricelist.getPricelistname());  //价目表名称
            goods.setDiscount(productpricelist.getDiscount());   //折扣
        }

        Product product;
        product=productService.getById(goods.getName());
        if(product!=null){
            goods.setName(product.getProductname());  //产品名称
            goods.setNo(product.getProductcode());  //产品编号
            goods.setStore(product.getStore());  //库存
            goods.setPrice(product.getPrice());  //原价
        }
        goodsService.updateById(goods);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        goodsService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        Goods goods = goodsService.getById(id);
        goods.setRealPrice(goods.getPrice().multiply(BigDecimal.valueOf(goods.getDiscount())));
        return Result.success(goods);
    }

    @GetMapping
    public Result findAll() {
        List<Goods> list = goodsService.findAll();
        return Result.success(list);
    }

    /**
     * 推荐产品
     * @return
     */
    @GetMapping("/recommend")
    public Result recommend() {
        List<Goods> list = goodsService.recommend();
        return Result.success(list);
    }

    /**
     * 推热销产品
     * @return
     */
    @GetMapping("/sales")
    public Result sales() {
        List<Goods> list = goodsService.sales();
        return Result.success(list);
    }

    /**
     * 根据分类id查询产品
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/byCategory/{id}")
    public Result findByCategory(@PathVariable Long id,
                                    @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        IPage<Goods> page = goodsService.pageByCategory(new Page<>(pageNum, pageSize), id);
        return Result.success(page);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(required = false, defaultValue = "") String name,

                           @RequestParam(required = false, defaultValue = "") String pricelistname,
                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        IPage<Goods> page = goodsService.findPage(new Page<>(pageNum, pageSize), name,pricelistname);

        return Result.success(page);
    }

}
