package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_order")
public class YNOrder {

    @TableId(type = IdType.AUTO)
    private Integer id;


    private String conditio;

    private String ordertype;

    private String customername;



    private String salesman;

    private String source;
    private String delivery;

    private Date submission;
    private Date deadline;

    private String unit;
    private String area;
    private String contactperson;
    private String contactnumber;
    private String notes;
    private String productname;
    private String quantity;








}