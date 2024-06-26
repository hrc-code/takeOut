package com.hrc.takeOut.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
/* 分类*/
@Data
public class Category {
        private Long id;
        private Integer type;
        private String name;
        private Integer sort;
        @TableField(fill = FieldFill.INSERT)
        private LocalDateTime createTime;
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime updateTime;
        @TableField(fill = FieldFill.INSERT)
        private Long createUser;
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private Long updateUser;
}
