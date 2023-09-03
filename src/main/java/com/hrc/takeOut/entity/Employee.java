package com.hrc.takeOut.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
/** 员工表*/
@Data
public class Employee {
        private Long id;
        private String name;
        private String  username;
        private String password;
        private String phone;
        private String sex;
        private String idNumber;
        private Integer status;
        @TableField(fill = FieldFill.INSERT)
        private LocalDateTime  createTime;
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime updateTime;
        @TableField(fill = FieldFill.INSERT)
        private Long createUser;
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private Long updateUser;
}
