package com.hrc.takeOut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrc.takeOut.exception.CustomException;
import com.hrc.takeOut.entity.Category;
import com.hrc.takeOut.entity.Dish;
import com.hrc.takeOut.entity.Setmeal;
import com.hrc.takeOut.mapper.CategoryMapper;
import com.hrc.takeOut.service.CategoryService;
import com.hrc.takeOut.service.DishService;
import com.hrc.takeOut.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private DishService dishService ;
    @Resource
    private SetmealService setmealService;

    /**删除分类
     * 分类必须为空才能删除，分类不能包含菜品，以及属于某个套餐*/
    @Override
    public void remove(Long id) {
        //检验分类是否包含菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        long dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount > 0) {
            throw new CustomException("该分类已包含菜品，无法删除");
        }
        log.info("需要删除的分类不包含菜品");
        //检验分类是否属于某个套餐
        LambdaQueryWrapper<Setmeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        long setMealCount = setmealService.count(setMealLambdaQueryWrapper);
        if (setMealCount > 0) {
            throw new CustomException("该分类属于某个菜品，无法删除");
        }
        log.info("需要删除的分类不属于任何一个套餐");
        super.removeById(id);
        log.info("成功删除分类 id :{}",id);
    }
}

