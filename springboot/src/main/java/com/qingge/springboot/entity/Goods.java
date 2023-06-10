package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName(value = "goods", autoResultMap = true)
public class Goods extends Model<Goods> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 产品名称 
      */

    private String name;

    /**
      * 产品描述 
      */
    private String description;

    /**
      * 产品编号 
      */
    private String no;


    /**
     * 价目表名称
     */

    private String pricelistname;


    /**
      * 原价 
      */
    private BigDecimal price;
    @TableField(exist = false)
    private BigDecimal realPrice;

    /**
      * 折扣 
      */
    private Double discount;

    /**
      * 库存 
      */
    private Integer store;

    /**
      * 点赞数 
      */
    private Integer praise;

    /**
      * 销量 
      */
    private Integer sales;

    /**
      * 分类id 
      */
    private Long categoryId;
    @TableField(exist = false)
    private String categoryName;

    /**
      * 产品图片 
      */
    private String imgs;

    /**
      * 创建时间 
      */
    private String createTime;

    private Boolean recommend;

}