package com.hrc.takeOut.controller;

import com.hrc.takeOut.core.commom.Result;
import com.hrc.takeOut.core.dto.DishDto;
import com.hrc.takeOut.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**菜品管理*/
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
        @Resource
        private DishService dishService;
        /**新增菜品*/
        @PostMapping
        public Result<String>  save(@RequestBody DishDto dishDto) {
                log.info("新增菜品");
                dishService.saveWithFlavor(dishDto);
                return Result.success("新增菜品成功");
        }
}
