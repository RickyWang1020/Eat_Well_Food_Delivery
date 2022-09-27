package com.abc.eatwell.service.impl;

import com.abc.eatwell.entity.DishFlavor;
import com.abc.eatwell.mapper.DishFlavorMapper;
import com.abc.eatwell.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
