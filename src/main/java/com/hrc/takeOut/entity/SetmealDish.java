package com.hrc.takeOut.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
/** 套餐菜品关系表*/
@Data
public class SetmealDish {
    private Long id;
    private Long setmealId;
    private String dishId;
    private String name;
    private Double price;
    private Integer copies;
    private Integer sort;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill =FieldFill.INSERT)
    private Long createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    private Integer isDeleted;
}
