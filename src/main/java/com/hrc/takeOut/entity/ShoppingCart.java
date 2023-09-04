package com.hrc.takeOut.entity;

import lombok.Data;

import java.time.LocalDateTime;

/** 购物车*/
@Data
public class ShoppingCart {
    private Long id;
    private Long userId;
    private Long dishId;
    private Long setmealId;
    private String name;
    private String image;
    private String dishFlavor;
    private Integer number;
    private Double amount;
    private LocalDateTime createTime;
}
