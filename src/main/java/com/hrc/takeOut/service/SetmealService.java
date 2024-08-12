package com.hrc.takeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrc.takeOut.model.dto.SetmealDto;
import com.hrc.takeOut.model.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal>{
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
