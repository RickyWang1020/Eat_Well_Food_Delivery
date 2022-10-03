package com.abc.eatwell.controller;

import com.abc.eatwell.common.R;
import com.abc.eatwell.entity.Orders;
import com.abc.eatwell.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * orders
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * user submit the orders
     * @param orders
     * @return
     */
    @PostMapping("submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("submit orders success!");
    }
}
