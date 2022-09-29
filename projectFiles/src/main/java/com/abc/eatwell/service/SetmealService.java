package com.abc.eatwell.service;

import com.abc.eatwell.dto.SetmealDto;
import com.abc.eatwell.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * add new setmeal, and save the relationship between setmeal and dishes
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * delete setmeal, and delete the relationship between setmeal and dishes
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
