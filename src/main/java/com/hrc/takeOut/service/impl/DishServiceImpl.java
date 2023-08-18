package com.hrc.takeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrc.takeOut.core.dto.DishDto;
import com.hrc.takeOut.entity.Dish;
import com.hrc.takeOut.entity.DishFlavor;
import com.hrc.takeOut.mapper.DishMapper;
import com.hrc.takeOut.service.DishFlavorService;
import com.hrc.takeOut.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Resource
    private DishFlavorService dishFlavorService;
    /** 新增菜品，同时保存对应的口味数据*/
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        super.save(dishDto);
        //保存菜品的基本信息到数据库之后mp会根据雪花算法生成菜品id
        Long dishId = dishDto.getId();
        //菜品口味，为菜品口味添加对应的菜品id
        List<DishFlavor> dishFlavors = dishDto.getFlavors().stream().peek((item) -> item.setDishId(dishId)).collect(Collectors.toList());
        //保存菜品口味
        dishFlavorService.saveBatch(dishFlavors);
        log.info("service 新增菜品完成");
    }
}
