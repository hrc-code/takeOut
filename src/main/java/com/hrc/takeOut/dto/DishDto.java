package com.hrc.takeOut.dto;

import com.hrc.takeOut.entity.Dish;
import com.hrc.takeOut.entity.DishFlavor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
/**新增菜品实体类
 * 菜品+菜品口味+菜品种类
 * */
@EqualsAndHashCode(callSuper = true)
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
}
