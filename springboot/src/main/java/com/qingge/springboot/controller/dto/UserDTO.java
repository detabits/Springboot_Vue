package com.qingge.springboot.controller.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qingge.springboot.entity.Menu;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
@TableName(value = "sys_user", autoResultMap = true)
public class UserDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;
    private String password;
    private String nickname;

    private String email;
    private String phone;
    private String address;
    private String createTime;

    private String avatar;
    private String token;
    private String role;

    private BigDecimal account;

    private List<Menu> menus;




}