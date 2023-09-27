package com.hrc.takeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hrc.takeOut.commom.Result;
import com.hrc.takeOut.entity.ShoppingCart;
import com.hrc.takeOut.service.ShoppingCartService;
import com.hrc.takeOut.utils.ThreadLocals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/** 购物车*/
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;
    /** 添加购物车
     * 业务逻辑：查询当前套餐或菜品是否在购物车，如果在则count++,否则使用使用默认值1*/
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
            //补充数据----登录用户id
            shoppingCart.setUserId(ThreadLocals.getCurrentId());
        // select * from  shopping_cart s_c where user_id = s_c.user_id  and setmeal_id = s_c.setmeal_id /  dish_id = s_c.dish_id
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, ThreadLocals.getCurrentId());
        //判断是将套餐添加到 购物车还是将 菜品添加到购物车
        Long dishId = shoppingCart.getDishId();
         if ( dishId != null) {
             //菜品id不为空说明是将菜品加入购物车
             shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
         } else {
             shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
         }
        // 查询当前套餐或菜品是否在购物车，如果在则count++,否则使用使用默认值1
        ShoppingCart shoppingCartOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
         if (shoppingCartOne != null) {
             //如果查出来的数据不为空说明当前商品已加入购物车
             Integer number = shoppingCartOne.getNumber();
             shoppingCartOne.setNumber(number+1);
             shoppingCartService.updateById(shoppingCartOne);
         } else {
             //数据库设置字段时购物车商品数量为1  可以不用 shoppingCart.setNumber(1)
             // 补充创建时间
             shoppingCart.setCreateTime(LocalDateTime.now());
             shoppingCartService.save(shoppingCart);
         }
         return  Result.success(shoppingCart);
    }
    /** 减少购物车数量*/
    @PostMapping("/sub")
    public Result<String> sub(@RequestBody ShoppingCart shoppingCart) {
        // 如果数量-1零则删除
        // delete * from shopping_cart s_c where user_id = s_c.user_id and setmeal_id = s_c.setmeal_id / s_c.dish_id
        //数量-1大于零则将count--
        //set s_c.number = s_c.number - 1 from ---
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, ThreadLocals.getCurrentId());
        //判断是套餐还是菜品
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCarteOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        Integer number = shoppingCarteOne.getNumber();
        if (number - 1 > 0) {
            shoppingCarteOne.setNumber(number - 1);
            shoppingCartService.updateById(shoppingCarteOne);
        } else {
            shoppingCartService.removeById(number);
        }
        return  Result.success("减少数量成功");
    }
    /** 查看购物车*/
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        // select * from shopping_cart s_c where user_id = s_c.user_id order by createTime asc;
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, ThreadLocals.getCurrentId());
        shoppingCartLambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return Result.success(shoppingCartList);
    }

    /** 清空购物车*/
    @DeleteMapping("/clean")
    public Result<String> clean() {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, ThreadLocals.getCurrentId());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return Result.success("清空购物车成功");
    }
}
