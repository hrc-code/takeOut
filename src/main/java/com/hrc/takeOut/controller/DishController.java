package com.hrc.takeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrc.takeOut.core.commom.Result;
import com.hrc.takeOut.core.dto.DishDto;
import com.hrc.takeOut.entity.Dish;
import com.hrc.takeOut.service.CategoryService;
import com.hrc.takeOut.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**菜品管理*/
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
        @Resource
        private DishService dishService;
        @Resource
        private CategoryService categoryService;
        /**新增菜品*/
        @PostMapping
        public Result<String>  save(@RequestBody DishDto dishDto) {
                log.info("开始新增菜品");
                dishService.saveWithFlavor(dishDto);
                log.info("结束新增菜品");
                return Result.success("新增菜品成功");
        }
        /**菜品信息分页查询*/
        @GetMapping("/page")
        public Result<Page<DishDto>> page(int page, int pageSize, String name) {
                //分页查询构造器对象
                Page<Dish> dishPage = new Page<>(page, pageSize);
                Page<DishDto> dishDtoPage = new Page<>();

                //条件构造器
                LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
                //添加模糊查询条件
                dishLambdaQueryWrapper.like(name != null, Dish::getName, name);
                //添加查询完后的排序条件
                dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
                //执行分页查询
                log.info("开始菜品信息分页查询");
                dishService.page(dishPage,dishLambdaQueryWrapper);
                log.info("结束菜品信息分页查询");
                //对象拷贝  分页查询的数据存放在records集合中
                BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
                //获取查询到的菜品数据
                List<Dish> records = dishPage.getRecords();
                //添加种类名称
                List<DishDto> dishDtoList = records.stream().map(dish -> {
                        DishDto dishDto = new DishDto();
                        BeanUtils.copyProperties(dish, dishDto);
                        //获取种类名称
                        Long categoryId = dish.getCategoryId();
                        log.info("开始菜品名称查询");
                        String categoryName = categoryService.getById(categoryId).getName();
                        log.info("结束菜品名称查询");
                        dishDto.setCategoryName(categoryName);
                        return dishDto;
                }).collect(Collectors.toList());
                dishDtoPage.setRecords(dishDtoList);
                return Result.success(dishDtoPage);
        }

        /**根据id查询菜品信息和对应的口味*/
        @GetMapping("/{id}")
        public Result<DishDto> get(@PathVariable Long id) {
                DishDto dishDto = dishService.getByIdWithFlavor(id);
                return Result.success(dishDto);
        }
        /**修改菜品*/
        @PutMapping
        public Result<String>  update(@RequestBody DishDto dishDto) {
                dishService.updateWithFlavor(dishDto);
                return  Result.success("修改菜品成功");
        }
}

