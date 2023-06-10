package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "sys_product")
public class Product extends Model<Product> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String productname;
    private String productabbreviation;
    private String productcode;
    private String productclassification;
    private String area;
    private Integer store;
    private String unit;
    private String isdisplayed;
    private BigDecimal price;
    private Date effectivedate;
    private Date expirydate;





}