package com.hrc.takeOut.dto;

import com.hrc.takeOut.entity.Setmeal;
import com.hrc.takeOut.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
