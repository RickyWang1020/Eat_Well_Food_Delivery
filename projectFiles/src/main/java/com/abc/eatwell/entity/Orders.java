package com.abc.eatwell.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * orders
 */
@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String number;

    // status: 1-waiting for payment, 2-waiting for delivery, 3-delivered, 4-completed, 5-cancelled
    private Integer status;

    private Long userId;

    private Long addressBookId;

    private LocalDateTime orderTime;

    private LocalDateTime checkoutTime;

    private Integer payMethod;

    private BigDecimal amount;

    private String remark;

    private String userName;

    private String phone;

    private String address;

    private String consignee;
}
