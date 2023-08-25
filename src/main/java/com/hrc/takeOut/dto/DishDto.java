package com.hrc.takeOut.dto;

import com.hrc.takeOut.entity.Dish;
import com.hrc.takeOut.entity.DishFlavor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
/** 菜品表+菜品口味表+分类表
 * */
@EqualsAndHashCode(callSuper = true)
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
}
