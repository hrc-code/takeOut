package com.hrc.takeOut.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Dish {
        private Long id;
        private String name;
        private Long categoryId;
        private Double price;
        private String code;
        private String image;
        private String description;
        private Integer status;
        private Integer sort;
        @TableField(fill = FieldFill.INSERT)
        private LocalDateTime createTime;
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime updateTime;
        @TableField(fill = FieldFill.INSERT)
        private Long createUser;
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private Long updateUser;
        private Integer isDeleted;
}
