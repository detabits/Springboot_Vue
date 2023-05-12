package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String productname;
    private String productabbreviation;
    private String area;
    private String productclassification;
    private String isstock;
    private String isdisplayed;
    private String unit;
    private String orderofproducts;
    private Date effectivedate;
    private Date expirydate;




}