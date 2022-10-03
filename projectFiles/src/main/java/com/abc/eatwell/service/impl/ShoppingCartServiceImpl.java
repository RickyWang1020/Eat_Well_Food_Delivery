package com.abc.eatwell.service.impl;

import com.abc.eatwell.entity.ShoppingCart;
import com.abc.eatwell.mapper.ShoppingCartMapper;
import com.abc.eatwell.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
