package com.abc.eatwell.service.impl;

import com.abc.eatwell.entity.Setmeal;
import com.abc.eatwell.mapper.SetmealMapper;
import com.abc.eatwell.service.SetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
