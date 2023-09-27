package com.hrc.takeOut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrc.takeOut.entity.AddressBook;
import com.hrc.takeOut.entity.OrderDetail;
import com.hrc.takeOut.entity.Orders;
import com.hrc.takeOut.entity.ShoppingCart;
import com.hrc.takeOut.entity.User;
import com.hrc.takeOut.exception.CustomException;
import com.hrc.takeOut.mapper.OrdersMapper;
import com.hrc.takeOut.service.AddressBookService;
import com.hrc.takeOut.service.OrderDetailService;
import com.hrc.takeOut.service.OrdersService;
import com.hrc.takeOut.service.ShoppingCartService;
import com.hrc.takeOut.service.UserService;
import com.hrc.takeOut.utils.ThreadLocals;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private UserService userService;
    @Resource
    private AddressBookService addressBookService;
    @Resource
    private OrderDetailService orderDetailService;

    /**
     * 功能：用户下单
     * 业务逻辑 将购物车表与下单用户相关的数据删除；将每个商品信息存入一条订单详细，并且补充订单号；添加一条订单表并且补充其所需要的字段
     * 表：订单表+购物车表+用户表+地址簿表+订单详细表
     */
    @Transactional
    @Override
    public void submit(Orders orders) {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, ThreadLocals.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new CustomException("购物车为空，不能下单");
        }
        //生成订单id,不使用雪花算法生成，因为订单数据需要商品总价格
        long orderId = IdWorker.getId();
        //商品总价格
        AtomicInteger amount = new AtomicInteger(0);
//        将每个商品信息存入一条订单详细，并且补充订单号
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            //为每条订单详细补充订单id
            orderDetail.setOrderId(orderId);
            //计算商品总价格
            amount.addAndGet(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
//        添加一条订单表并且补充其所需要的字段
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        //TODO (hrc,2023/9/22,0:03) 消除魔法值
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(ThreadLocals.getCurrentId());
        orders.setNumber(String.valueOf(orderId));
        User user = userService.getById(ThreadLocals.getCurrentId());
        orders.setUserName(user.getName());
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new CustomException("用户地址错误，不能下单");
        }
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入一条数据
        super.save(orders);
        //向订单详细表插入多条数据
        orderDetailService.saveBatch(orderDetailList);
        //将购物车表与下单用户相关的数据删除
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
    }
}
