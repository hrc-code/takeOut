package com.hrc.takeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrc.takeOut.entity.OrderDetail;
import com.hrc.takeOut.mapper.OrderDetailMapper;
import com.hrc.takeOut.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
