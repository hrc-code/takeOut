package com.hrc.takeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrc.takeOut.model.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
