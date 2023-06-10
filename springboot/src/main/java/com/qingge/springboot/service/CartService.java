package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.entity.Cart;
import com.qingge.springboot.entity.Goods;
import com.qingge.springboot.mapper.CartMapper;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService extends ServiceImpl<CartMapper, Cart> {

    @Resource
    private GoodsService goodsService;

    /**
     * 计算购物车产品总价和优惠金额
     * @param carts
     * @return
     * @throws JSONException
     */
    public Map<String, Object> findAll(List<Cart> carts) throws JSONException {
        BigDecimal totalPrice = new BigDecimal(0);
        BigDecimal originPrice = new BigDecimal(0);
        String productname="";   //产品名称
        Integer quantity=0;      //数量

        Map<String, Object> res = new HashMap<>();

        for (Cart cart : carts) {
            quantity=cart.getCount();   //数量

            Long goodsId = cart.getGoodsId();
            Goods goods = goodsService.getById(goodsId);
            goods.setRealPrice(goods.getPrice().multiply(BigDecimal.valueOf(goods.getDiscount())));
            cart.setGoods(goods);

            productname=goods.getName();

            totalPrice = totalPrice.add(goods.getRealPrice().multiply(BigDecimal.valueOf(cart.getCount())));
            originPrice = originPrice.add(goods.getPrice().multiply(BigDecimal.valueOf(cart.getCount())));
        }

        res.put("list", carts);  // 购物车列表
        res.put("totalPrice", totalPrice);  // 总价
        res.put("discount", originPrice.subtract(totalPrice));    // 折扣优惠金额
        res.put("productname", productname);  // 购物车列表
        res.put("quantity", quantity);  // 购物车列表
        return res;
    }
}
