package com.hrc.takeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrc.takeOut.commom.Result;
import com.hrc.takeOut.entity.Orders;
import com.hrc.takeOut.service.OrdersService;
import com.hrc.takeOut.utils.ThreadLocals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrdersService ordersService;
    /** 查询用户的订单*/
    @GetMapping("/userPage")
    public Page<Orders> page(int page, int pageSize) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId, ThreadLocals.getCurrentId());
        ordersLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(ordersPage, ordersLambdaQueryWrapper);
        return ordersPage;
    }
    /** 用户下单*/
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return Result.success("下单成功");
    }

}
