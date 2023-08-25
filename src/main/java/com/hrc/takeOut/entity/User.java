package com.hrc.takeOut.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String phone;
    private String sex;
    private String idNumber;
    private String avatar;
    private Integer status;
}
