package com.hrc.takeOut.model.dto;

import com.hrc.takeOut.model.entity.Setmeal;
import com.hrc.takeOut.model.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
