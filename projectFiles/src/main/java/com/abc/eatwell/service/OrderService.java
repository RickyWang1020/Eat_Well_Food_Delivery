package com.abc.eatwell.service;

import com.abc.eatwell.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Orders> {

    /**
     * user submit the orders
     * @param orders
     * @return
     */
    public void submit(Orders orders);
}
