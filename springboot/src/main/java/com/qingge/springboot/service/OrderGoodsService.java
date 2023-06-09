package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.entity.Cart;
import com.qingge.springboot.entity.Goods;
import com.qingge.springboot.entity.OrderGoods;
import com.qingge.springboot.mapper.OrderGoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderGoodsService extends ServiceImpl<OrderGoodsMapper, OrderGoods> {

    @Resource
    private OrderGoodsMapper orderGoodsMapper;
    @Resource
    private GoodsService goodsService;

    /**
     * 根据订单id查询订单关联的产品列表
     * @param orderId
     * @return
     */
    public List<Cart> findByOrderId(Long orderId) {
        List<Cart> carts = new ArrayList<>();
        List<OrderGoods> orderGoods = orderGoodsMapper.findGoodsByOrderId(orderId);
        for (OrderGoods orderGood : orderGoods) {
            Long goodsId = orderGood.getGoodsId();
            Goods goods = goodsService.getById(goodsId);
            Cart cart = new Cart();
            cart.setGoods(goods);
            cart.setGoodsId(goodsId);
            cart.setCount(orderGood.getCount());
            carts.add(cart);
        }
        return carts;
    }
}
