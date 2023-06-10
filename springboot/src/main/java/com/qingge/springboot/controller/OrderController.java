package com.qingge.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.dto.PreOrderQo;
import com.qingge.springboot.entity.*;
import com.qingge.springboot.exception.CustomException;
import com.qingge.springboot.service.*;
import org.json.JSONException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import com.qingge.springboot.entity.Product;
import com.qingge.springboot.service.ProductService;
import com.qingge.springboot.entity.Goods;
import com.qingge.springboot.service.GoodsService;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Resource
    private OrderService orderService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;
    @Resource
    private CartService cartService;
    @Resource
    private OrderGoodsService orderGoodsService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private ProductService productService;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        username=userService.findusername(username);  //特事特办
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }




    @Transactional
    @PostMapping
    public Result save(@RequestBody Order order) {
        if(order.getSalesman()==null) {
            order.setUserId(getUser().getId());
            order.setOrderNo(DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6));
            order.setCreateTime(DateUtil.now());

            String cartsStr = order.getCarts();
            List<Cart> carts = JSONUtil.toBean(cartsStr, new TypeReference<List<Cart>>() {
            }, true);

            order.setOrdertype("在线销售");                      //订单类型
            order.setCustomername(getUser().getNickname());    //客户名称
            order.setSalesman("无");                           //业务员
            order.setSource("线上");                           //订单来源
            order.setDelivery("快递");                         //提货方式
            order.setSubmission(DateUtil.now());              //提交日期

            String dateStr = DateUtil.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
            // 将日期时间向后推移 7 天
            LocalDateTime newDateTime = dateTime.plusDays(7);
            // 将新日期时间对象转换回字符串格式
            String newDateStr = newDateTime.format(formatter);
            order.setDeadline(newDateStr);              //提交截止日期

            Product product;
            product = productService.getOne(Wrappers.<Product>lambdaQuery().eq(Product::getProductname, order.getProductname()));
            //根据名称找产品id
            order.setUnit(product.getUnit());          //提货单位
            order.setArea(product.getArea());          //提货厂区
            order.setNotes("无");                      //备注

            orderService.save(order);

            for (Cart cart : carts) {
                Integer count = cart.getCount();
                Long goodsId = cart.getGoodsId();

                // 扣库存
                Goods goods = goodsService.getById(goodsId);
                if (goods.getStore() - cart.getCount() < 0) {
                    throw new CustomException("-1", "库存不足");
                }
                goods.setStore(goods.getStore() - cart.getCount());
                goods.setSales(goods.getSales() + cart.getCount());
                goodsService.updateById(goods);

                OrderGoods orderGoods = new OrderGoods();
                orderGoods.setOrderId(order.getId());
                orderGoods.setGoodsId(goodsId);
                orderGoods.setCount(count);
                orderGoodsService.save(orderGoods);
            }

            if (order.getType() == 1) {  // 1表示购物车，0表示直接购买
                // 提交订单时清空个人的购物车产品
                cartService.remove(Wrappers.<Cart>lambdaUpdate().eq(Cart::getUserId, getUser().getId()));
            }

            return Result.success(order);
        }
        else{
            order.setUserId(getUser().getId());
            order.setOrderNo(DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6));
            order.setCreateTime(DateUtil.now());
            order.setSource("线下");                           //订单来源

            Product product;
            product = productService.getById(order.getProductname());
            //产品名称是数字形式，恰好与id对应
            order.setProductname(product.getProductname());  //产品名称数字变字符串
            order.setUnit(product.getUnit());          //提货单位
            order.setArea(product.getArea());          //提货厂区


            Goods goods;  //只能先保持现状   产品名称id->产品id    产品名称->产品名称->产品id
            //goods = goodsService.getById(order.getProductname());
            goods =goodsService.getOne(Wrappers.<Goods>lambdaQuery().eq(Goods::getName, order.getProductname()));
            BigDecimal price;
            Double discount;
            int quantity;
            BigDecimal total_price;
            price=goods.getPrice();
            discount=goods.getDiscount();
            quantity=order.getQuantity();
            BigDecimal bd_discount = BigDecimal.valueOf(discount); // 将discount转换为BigDecimal类型
            total_price = price.multiply(bd_discount).multiply(BigDecimal.valueOf(quantity)); // 使用BigDecimal的multiply方法进行乘法运算
             // 如果需要保留小数位数，可以使用setScale方法设置精度
             // 例如，保留2位小数：
            total_price = total_price.setScale(2, RoundingMode.HALF_UP);
            order.setTotalPrice( total_price);

            if(order.getNotes()==null)
            {
                order.setNotes("无");
            }
            orderService.save(order);
            return Result.success(order);
        }
    }

    @PutMapping
    public Result update(@RequestBody Order order) {
        orderService.updateById(order);
        return Result.success();
    }

    /**
     * 付款
     * @param id
     * @return
     */
    @Transactional
    @PutMapping("/pay/{id}")
    public Result pay(@PathVariable Long id) {
        Order order = orderService.getById(id);
        BigDecimal totalPrice = order.getTotalPrice();

       Integer userId = getUser().getId();
        User user = userService.findById(userId);

        if (user.getAccount().compareTo(totalPrice) <= 0) {
            throw new CustomException("-1", "余额不足");
        }

        user.setAccount(user.getAccount().subtract(totalPrice)); // 设置用户余额
        userService.updateById(user);
        order.setState("待发货");
        orderService.updateById(order);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        orderService.removeById(id);
        return Result.success();
    }

    /**
     * 获取订单的确认信息
     *
     * @return
     */
    @PostMapping("/pre")
    public Result pre(@RequestBody PreOrderQo preOrderQo) throws JSONException {
        String cartsStr = preOrderQo.getCarts();
        // 讲前台传来的json字符串转换成 list对象
        List<Cart> carts = JSONUtil.toBean(cartsStr, new TypeReference<List<Cart>>() {
        }, true);
        Map<String, Object> all = cartService.findAll(carts);
        return Result.success(all);
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }

    @GetMapping
    public Result findAll() {
        List<Order> list = orderService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result findPage(   @RequestParam(required = false, defaultValue = "") String orderNo,
                              @RequestParam(required = false, defaultValue = "") String salesman,
                              @RequestParam(required = false, defaultValue = "") String productname,
                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Order> query = Wrappers.<Order>lambdaQuery().orderByDesc(Order::getId);
        if (StrUtil.isNotBlank(orderNo)) {
            query.like(Order::getOrderNo, orderNo);
        }
        if (StrUtil.isNotBlank(salesman)) {
            query.like(Order::getSalesman, salesman);
        }
        if (StrUtil.isNotBlank(productname)) {
            query.like(Order::getProductname, productname);
        }
        IPage<Order> page = orderService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }

    /**
     * 前台查询订单列表
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/page/front")
    public Result findPageFront(@RequestParam(required = false, defaultValue = "") String state,
                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Order> query = Wrappers.<Order>lambdaQuery().orderByDesc(Order::getId);
        query.eq(Order::getUserId, getUser().getId());
        // 根据状态查询
        if (StrUtil.isNotBlank(state)) {
            query.eq(Order::getState, state);
        }
        IPage<Order> page = orderService.page(new Page<>(pageNum, pageSize), query);

        for (Order order : page.getRecords()) {
            Long orderId = order.getId();
            List<Cart> carts = orderGoodsService.findByOrderId(orderId);
            order.setCarts(JSONUtil.toJsonStr(carts));
        }
        return Result.success(page);
    }




}
