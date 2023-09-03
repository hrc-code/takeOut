package com.hrc.takeOut.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
