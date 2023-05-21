package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


@Data
@TableName("order_goods")
public class OrderGoods extends Model<OrderGoods> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 订单id 
      */
    private Long orderId;

    /**
      * 商品id 
      */
    private Long goodsId;

    /**
      * 数量 
      */
    private Integer count;

}