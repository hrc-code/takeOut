package com.hrc.takeOut.controller;

import com.hrc.takeOut.commom.Result;
import com.hrc.takeOut.dto.SetmealDto;
import com.hrc.takeOut.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/** 套餐管理*/
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    /** 新增套餐*/
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("开始调用新增套餐接口");
        setmealService.saveWithDish(setmealDto);
        log.info("结束调用新增套餐接口");
        return  Result.success("新增菜品成功");
    }
}
