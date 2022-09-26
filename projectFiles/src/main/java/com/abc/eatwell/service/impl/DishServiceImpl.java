package com.abc.eatwell.service.impl;

import com.abc.eatwell.entity.Dish;
import com.abc.eatwell.mapper.DishMapper;
import com.abc.eatwell.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
