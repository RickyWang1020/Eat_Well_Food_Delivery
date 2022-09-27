package com.abc.eatwell.controller;

import com.abc.eatwell.common.R;
import com.abc.eatwell.dto.DishDto;
import com.abc.eatwell.entity.Category;
import com.abc.eatwell.entity.Dish;
import com.abc.eatwell.service.CategoryService;
import com.abc.eatwell.service.DishFlavorService;
import com.abc.eatwell.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * dish management
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * add new dish
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("add new dish success");
    }

    /**
     * page segmentation of dish information
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        // constructor object
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoInfo = new Page<>();

        // conditions constructor
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // add filtering condition
        queryWrapper.like(name != null, Dish::getName, name);
        // add sorting condition
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        // query the data and put it into the pageInfo
        dishService.page(pageInfo, queryWrapper);

        // copy the properties of pageInfo to dishDtoInfo, ignoring the "records" field
        BeanUtils.copyProperties(pageInfo, dishDtoInfo, "records");

        // manually process the records for each object in dishDtoInfo
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> lst = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId(); // category id
            // get the category object by id
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoInfo.setRecords(lst);

        return R.success(dishDtoInfo);
    }

    /**
     * query for the dish information and flavor information based on given id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * update dish
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("add new dish success");
    }
}
