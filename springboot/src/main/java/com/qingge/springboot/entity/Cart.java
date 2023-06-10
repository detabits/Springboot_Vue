package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


@Data
@TableName("cart")
public class Cart extends Model<Cart> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 产品id 
      */
    private Long goodsId;

    /**
      * 用户id 
      */
    private Long userId;

    /**
      * 产品数量 
      */
    private Integer count;
    private String createTime;

    @TableField(exist = false)
    private Goods goods;

}