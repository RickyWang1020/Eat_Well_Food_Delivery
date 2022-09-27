package com.abc.eatwell.dto;

import com.abc.eatwell.entity.Dish;
import com.abc.eatwell.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
