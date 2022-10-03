package com.abc.eatwell.controller;

import com.abc.eatwell.common.BaseContext;
import com.abc.eatwell.common.R;
import com.abc.eatwell.entity.ShoppingCart;
import com.abc.eatwell.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * shopping cart
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * add item to shopping cart
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {

        // specify which user's shopping cart
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);


        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            // if a dish is added to the shopping cart
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        }
        else {
            // if a combo is added to the shopping cart
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        // query whether current dish/combo is in the shopping cart
        ShoppingCart cart = shoppingCartService.getOne(lambdaQueryWrapper);

        if (cart != null) {
            // if yes, then add one to the quantity
            Integer number = cart.getNumber();
            cart.setNumber(number + 1);
            shoppingCartService.updateById(cart);
        }
        else {
            // if no, then add the dish/combo to the shopping cart
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cart = shoppingCart;
        }
        return R.success(cart);
    }

    /**
     * view the current user's shopping cart
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> lst = shoppingCartService.list(queryWrapper);

        return R.success(lst);
    }

    /**
     * clear the shopping cart
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("clear shopping cart success!");
    }
}
