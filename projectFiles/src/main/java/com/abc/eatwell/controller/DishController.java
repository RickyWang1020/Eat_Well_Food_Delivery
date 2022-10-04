package com.abc.eatwell.controller;

import com.abc.eatwell.common.R;
import com.abc.eatwell.dto.DishDto;
import com.abc.eatwell.entity.Category;
import com.abc.eatwell.entity.Dish;
import com.abc.eatwell.entity.DishFlavor;
import com.abc.eatwell.service.CategoryService;
import com.abc.eatwell.service.DishFlavorService;
import com.abc.eatwell.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * add new dish
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {

        dishService.saveWithFlavor(dishDto);

        // clear all the cache for all dishes
        // Set keys = redisTemplate.keys("dish_*");
        // redisTemplate.delete(keys);

        // only clear the cache for dishes under one category
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

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

        // clear all the cache for all dishes
        // Set keys = redisTemplate.keys("dish_*");
        // redisTemplate.delete(keys);

        // only clear the cache for dishes under one category
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("add new dish success");
    }

    /**
     * query for dish information based on conditions
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        // query for all the dishes in this category
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        // the dish's status must be 1 (available)
//        queryWrapper.eq(Dish::getStatus, 1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> lst = dishService.list(queryWrapper);
//
//        return R.success(lst);
//    }

    /**
     * query for dish information based on conditions
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {

        List<DishDto> dishDtoList;

        // get cached data from Redis
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        // if the cached data is not empty, then just return it
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        }

        // otherwise, query from the tables
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // query for all the dishes in this category
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        // the dish's status must be 1 (available)
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> lst = dishService.list(queryWrapper);

        dishDtoList = lst.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId(); // category id
            // get the category object by id
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            // current dish's id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        // and save the queried dishes to Redis
        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }
}
