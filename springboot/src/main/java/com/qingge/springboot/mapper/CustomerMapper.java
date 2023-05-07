package com.qingge.springboot.mapper;

import com.qingge.springboot.entity.Customer;
import com.qingge.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;




@Mapper
public interface CustomerMapper  extends BaseMapper<Customer>{


}