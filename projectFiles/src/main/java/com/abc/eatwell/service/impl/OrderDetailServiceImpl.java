package com.abc.eatwell.service.impl;

import com.abc.eatwell.entity.OrderDetail;
import com.abc.eatwell.mapper.OrderDetailMapper;
import com.abc.eatwell.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
