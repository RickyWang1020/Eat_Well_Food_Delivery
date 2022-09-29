package com.abc.eatwell.dto;

import com.abc.eatwell.entity.Setmeal;
import com.abc.eatwell.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
