package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "sys_user", autoResultMap = true)
public class User {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    //@JsonIgnore
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;

    private String createTime;

    private String avatar;  //头像q

    private String role;   //角色

    private BigDecimal account;

    private String tags;   //标签
}