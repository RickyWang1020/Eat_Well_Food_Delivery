package com.abc.eatwell.service;

import com.abc.eatwell.dto.DishDto;
import com.abc.eatwell.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    // add new dish, and insert the corresponding flavor data
    // need to operate on two tables: dish, dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    // query for the dish information and flavor information based on given id
    public DishDto getByIdWithFlavor(Long id);

    // update dish information, and update flavor information
    public void updateWithFlavor(DishDto dishDto);
}
