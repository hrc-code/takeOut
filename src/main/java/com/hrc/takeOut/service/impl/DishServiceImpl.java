package com.hrc.takeOut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrc.takeOut.dto.DishDto;
import com.hrc.takeOut.entity.Dish;
import com.hrc.takeOut.entity.DishFlavor;
import com.hrc.takeOut.mapper.DishMapper;
import com.hrc.takeOut.service.DishFlavorService;
import com.hrc.takeOut.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        log.info("开始保存菜品信息");
        super.save(dishDto);
        log.info("结束保存菜品信息");
        //保存菜品的基本信息到数据库之后mp会根据雪花算法生成菜品id
        Long dishId = dishDto.getId();
        //菜品口味，为菜品口味添加对应的菜品id
        List<DishFlavor> dishFlavors = dishDto.getFlavors().stream().peek((item) -> item.setDishId(dishId)).collect(Collectors.toList());
        //保存菜品口味
        log.info("开始保存菜品口味");
        dishFlavorService.saveBatch(dishFlavors);
        log.info("结束保存菜品口味");
    }

    /*根据菜品id查询菜品信息和对应的菜品口味*/
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        log.info("开始菜品查询");
        Dish dish = super.getById(id);
        log.info("结束菜品查询");
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        log.info("开始菜品口味查询");
         //条件包装器
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        log.info("结束菜品口味查询");
        dishDto.setFlavors(dishFlavorList);
        return dishDto;

    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        log.info("开始更新菜品信息");
        super.updateById(dishDto);
        log.info("结束更新菜品信息");
        //条件包装器  先删除旧的菜品口味
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
       //使用菜品id连接菜品口味表
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        log.info("开始删除旧的菜品口味");
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        log.info("结束删除旧的菜品口味");
        //为菜品口味添加菜品id
        List<DishFlavor> dishFlavorList = dishDto.getFlavors().stream().peek(dishFlavor -> dishFlavor.setDishId(dishDto.getId())).collect(Collectors.toList());
        log.info("开始更新菜品口味表");
        dishFlavorService.saveBatch(dishFlavorList);
        log.info("结束更新菜品口味表");
    }
}
