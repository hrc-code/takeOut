package com.hrc.takeOut.entity;

import lombok.Data;

/** 订单明细*/
@Data
public class OrderDetail {
    private Long id;
    private Long orderId;
    private Long dishId;
    private Long setmealId;
    private String name;
    private String image;
    private String dishFlavor;
    private Integer number;
    private Double amount;
}
