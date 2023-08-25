package com.hrc.takeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrc.takeOut.dto.SetmealDto;
import com.hrc.takeOut.entity.Setmeal;
import com.hrc.takeOut.entity.SetmealDish;
import com.hrc.takeOut.mapper.SetmealMapper;
import com.hrc.takeOut.service.SetmealDishService;
import com.hrc.takeOut.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
   @Resource
   private SetmealDishService setmealDishService;
    /** 新增套餐，同时保存套餐和菜品的关联关系*/
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        log.info("开始新增套餐表");
        super.save(setmealDto);
        log.info("结束新增套餐表");
        //为套餐菜品关系表添加套餐id
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes().stream().peek(setmealDish -> setmealDish.setSetmealId(setmealDto.getId())).collect(Collectors.toList());
        log.info("开始批量新增---套餐菜品关系表");
        setmealDishService.saveBatch(setmealDishList);
        log.info("结束批量新增--套餐菜品关系表");

    }
}
