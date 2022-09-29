package com.abc.eatwell.service.impl;

import com.abc.eatwell.common.CustomException;
import com.abc.eatwell.dto.SetmealDto;
import com.abc.eatwell.entity.Setmeal;
import com.abc.eatwell.entity.SetmealDish;
import com.abc.eatwell.mapper.SetmealMapper;
import com.abc.eatwell.service.SetmealDishService;
import com.abc.eatwell.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * add new setmeal, and save the relationship between setmeal and dishes
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // save setmeal information
        this.save(setmealDto);

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList = setmealDishList.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // save the relationship between setmeal and dish
        setmealDishService.saveBatch(setmealDishList);
    }

    /**
     * delete setmeal, and delete the relationship between setmeal and dishes
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // query for the setmeal status, see if it can be deleted (status = 0, can delete)
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);
        // if cannot delete, throw exception
        if (count > 0) {
            throw new CustomException("Combo is selling, cannot delete");
        }

        // if can delete, delete from the setmeal
        this.removeByIds(ids);

        // then delete from the relationship
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(lambdaQueryWrapper);

    }
}
