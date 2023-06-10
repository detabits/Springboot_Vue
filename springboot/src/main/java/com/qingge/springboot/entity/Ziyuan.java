package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "ziyuan", autoResultMap = true)
public class Ziyuan {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String ziyuancode;
    private String ziyuanname;
    private String ziyuantype;
    private String authorizationcode;

}