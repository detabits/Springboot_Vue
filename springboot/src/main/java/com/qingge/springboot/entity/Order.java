package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_order")
public class Order extends Model<Order> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 订单编号 
      */
    private String orderNo;

    /**
      * 总价 
      */
    private BigDecimal totalPrice;

    /**
      * 下单人id 
      */
    private Integer userId;

    /**
      * 联系人 
      */
    private String linkUser;

    /**
      * 联系电话 
      */
    private String linkPhone;

    /**
      * 送货地址 
      */
    private String linkAddress;

    /**
      * 状态 
      */
    private String state;

    /**
      * 创建时间 
      */
    private String createTime;

    @TableField(exist = false)
    private String carts;

    @TableField(exist = false)
    private Integer type;

    private String ordertype;     //订单类型
    private String customername;  //客户名称
    private String salesman;      //业务员
    private String source;        //订单来源
    private String delivery;      //提货方式

    private String submission;    //提交日期
    private String deadline;      //提交截至日期
    private String productname;   //产品名称
    private Integer quantity;      //数量
    private String unit;        //提货单位
    private String area;        //提货厂区
    private String notes;       //备注
}