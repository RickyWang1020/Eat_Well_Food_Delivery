package com.abc.eatwell.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * order details
 */
@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long orderId;

    private Long dishId;

    private Long setmealId;

    private String dishFlavor;

    private Integer number; // quantity

    private BigDecimal amount; // money

    private String image;
}
