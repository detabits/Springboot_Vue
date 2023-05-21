package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.entity.Address;
import com.qingge.springboot.mapper.AddressMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AddressService extends ServiceImpl<AddressMapper, Address> {

    @Resource
    private AddressMapper addressMapper;

}
