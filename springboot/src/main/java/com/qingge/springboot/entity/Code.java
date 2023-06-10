package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "code", autoResultMap = true)
public class Code {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String codeid;
    private String codename;
    private String itemid;
    private String itemname;

}