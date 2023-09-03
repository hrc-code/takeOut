package com.hrc.takeOut.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**地址簿*/
@Data
public class AddressBook {
        private Long id;
        private Long userId;
        private String sex;
        private String phone;
        private String consignee;
        private String provinceCode;
        private String provinceName;
        private String cityCode;
        private String cityName;
        private String districtCode;
        private String districtName;
        private String detail;
        private String label;
        private String isDefault;
        @TableField(fill = FieldFill.INSERT)
        private LocalDateTime createTime;
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime updateTime;
        @TableField(fill = FieldFill.INSERT)
        private Long createUser;
        @TableField( fill = FieldFill.INSERT_UPDATE)
        private Long updateUser;
        private Integer isDeleted;
}
