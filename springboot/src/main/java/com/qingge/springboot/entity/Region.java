package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "region", autoResultMap = true)
public class Region {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String province;
    private String city;
    private String county;


}