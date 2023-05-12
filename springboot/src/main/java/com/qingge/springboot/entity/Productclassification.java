package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_productclassification")
public class Productclassification {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String categoryname;
    private String level;
    private String leveldescription;
    private Date effectivedate;
    private Date expirydate;


}