package com.hrc.takeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrc.takeOut.dto.DishDto;
import com.hrc.takeOut.entity.Dish;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

     DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
