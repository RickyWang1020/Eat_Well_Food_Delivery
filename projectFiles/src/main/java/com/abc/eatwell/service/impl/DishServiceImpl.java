package com.abc.eatwell.service.impl;

import com.abc.eatwell.dto.DishDto;
import com.abc.eatwell.entity.Dish;
import com.abc.eatwell.entity.DishFlavor;
import com.abc.eatwell.mapper.DishMapper;
import com.abc.eatwell.service.DishFlavorService;
import com.abc.eatwell.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * add new dish, and insert the corresponding flavor data
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // save the basic info of the dish to "dish" table
        this.save(dishDto);
        Long dishId = dishDto.getId();

        List<DishFlavor> dishFlavor = dishDto.getFlavors();
        dishFlavor = dishFlavor.stream().map(item -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // save the flavor data to "dish_flavor" table
        dishFlavorService.saveBatch(dishFlavor);
    }

    /**
     * query for the dish information and flavor information based on given id
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // query basic info from "dish" table
        Dish dish = this.getById(id);

        // copy the info to a new dishDto object
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // query for the flavor info from "dish_flavor" table
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * update dish information, and update flavor information
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {

        // update base information in "dish" table
        this.updateById(dishDto);

        // clear this dish's flavor data in "dish_flavor" table
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        // add the current new flavor data in "dish_flavor" table
        List<DishFlavor> dishFlavor = dishDto.getFlavors();
        dishFlavor = dishFlavor.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishFlavor);
    }
}
