package com.hrc.takeOut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrc.takeOut.dto.SetmealDto;
import com.hrc.takeOut.entity.Setmeal;
import com.hrc.takeOut.entity.SetmealDish;
import com.hrc.takeOut.exception.CustomException;
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
    /**删除套餐，同时删除套餐和菜品相关联数据
     * 不能删除正在售卖的套餐
     * 参数：套餐id
     * 表：套餐表+套餐菜品关系表*/
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //判断是否含有在售套餐 select count(*) from setmeal where id in () and status = 1;
        LambdaQueryWrapper<Setmeal> setmealambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealambdaQueryWrapper.in(Setmeal::getId,ids);
        setmealambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        log.info("开始统计数量---套餐表");
        long setmealCount = super.count(setmealambdaQueryWrapper);
        log.info("结束统计数量--套餐表");
        if (setmealCount > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        log.info("开始批量删除---套餐表");
        super.removeByIds(ids);
        log.info("结束批量删除--套餐表");
        //开始操作套餐菜品关系比表 delete from setmeal_dish where setmeal_id in ();
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        log.info("开始批量删除---套餐关系表");
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
        log.info("结束批量删除---套餐关系表");
    }
}
