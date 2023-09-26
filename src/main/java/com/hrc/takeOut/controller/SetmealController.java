package com.hrc.takeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrc.takeOut.commom.Result;
import com.hrc.takeOut.dto.SetmealDto;
import com.hrc.takeOut.entity.Category;
import com.hrc.takeOut.entity.Setmeal;
import com.hrc.takeOut.entity.SetmealDish;
import com.hrc.takeOut.exception.CustomException;
import com.hrc.takeOut.service.CategoryService;
import com.hrc.takeOut.service.SetmealDishService;
import com.hrc.takeOut.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** 套餐管理*/
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Resource
    private SetmealService setmealService;
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private CategoryService categoryService;

    /** 新增套餐*/
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("开始调用新增套餐接口");
        setmealService.saveWithDish(setmealDto);
        log.info("结束调用新增套餐接口");
        return  Result.success("新增菜品成功");
    }
    /**套餐分页查询
     * 表：套餐表+套餐菜品关系表+分类表*/
    @Transactional
    @GetMapping("/page")
    public Result<Page<SetmealDto>> page(int page, int pageSize, String name) {
        //分页构造器对象
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //模糊查询
        setmealLambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        //查完之后数据的排序
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        log.info("开始分页查询---套餐表");
        setmealService.page(setmealPage, setmealLambdaQueryWrapper);
        log.info("结束分页查询---套餐表");
        //对象拷贝
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
       //为返回值补充菜品和分类名
        List<SetmealDto> setmealDtoList = setmealPage.getRecords().stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            //补充每个套餐所包含的菜品
            LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
            log.info("开始批量查询---套餐菜品关系表");
            List<SetmealDish> setmealDishListlist = setmealDishService.list(setmealDishLambdaQueryWrapper);
            log.info("结束批量查询---套餐关系表");
            setmealDto.setSetmealDishes(setmealDishListlist);
            //为每个套餐补充分类名
            Long categoryId = setmeal.getCategoryId();
            log.info("开始查询---分类表");
            Category category = categoryService.getById(categoryId);
            log.info("结束查询---分类表");
            if (category != null)
                setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        //为返回值设置records
        setmealDtoPage.setRecords(setmealDtoList);
        return Result.success(setmealDtoPage);
    }
    /**删除套餐
     * 表：套餐表+套餐菜品关系表
     * 参数： 套餐id*/
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("开始调用删除套餐接口");
        setmealService.removeWithDish(ids);
        log.info("结束调用删除套餐接口");
        return Result.success("删除套餐成功");
    }
    /**  查询套餐*/
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal) {
        //select * from setmeal s where category_id = s.category_id and status = s.status and order by updateTime desc;
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId());
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus, setmeal.getStatus());
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(setmealLambdaQueryWrapper);
        return Result.success(setmealList);
    }
    /** 根据id查询套餐*/
    @GetMapping("/{id}")
    public Result<Setmeal> getById(@PathVariable Long id) {
        if (Objects.isNull(id))
            new CustomException("参数异常");
        log.info("开始查询套餐");
        Setmeal setmeal = setmealService.getById(id);
        log.info("结束查询套餐");
        return Result.success(setmeal);
    }
}
