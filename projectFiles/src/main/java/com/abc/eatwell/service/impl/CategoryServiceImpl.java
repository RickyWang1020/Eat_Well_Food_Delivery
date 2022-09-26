package com.abc.eatwell.service.impl;

import com.abc.eatwell.common.CustomException;
import com.abc.eatwell.entity.Category;
import com.abc.eatwell.entity.Dish;
import com.abc.eatwell.entity.Setmeal;
import com.abc.eatwell.mapper.CategoryMapper;
import com.abc.eatwell.service.CategoryService;
import com.abc.eatwell.service.DishService;
import com.abc.eatwell.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * delete category based on id, do checks before deletion
     * @param ids
     */
    @Override
    public void remove(Long ids) {

        // add query conditions for querying dishes
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        int countDish = dishService.count(dishLambdaQueryWrapper);
        // if current category has connected to dishes, throw an exception
        if (countDish > 0) {
            throw new CustomException("Current category is associated with some Dish, CANNOT delete");
        }

        // add query conditions for querying combo
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        int countSet = setmealService.count(setmealLambdaQueryWrapper);
        // if current category has connected to combo, throw an exception
        if (countSet > 0) {
            throw new CustomException("Current category is associated with some Combo, CANNOT delete");
        }

        // otherwise, normal delete
        super.removeById(ids);

    }
}
