package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_productpricelist")
public class Productpricelist {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String pricelistname;
    private String area;
    private String pricelisttype;
    /**
     * 折扣
     */
    private Double discount;
    private String pricelistdescription;
    private String effectivedate;
    private String expirydate;






}