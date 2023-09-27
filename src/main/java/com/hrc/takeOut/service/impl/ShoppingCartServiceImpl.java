package com.hrc.takeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrc.takeOut.entity.ShoppingCart;
import com.hrc.takeOut.mapper.ShoppingCartMapper;
import com.hrc.takeOut.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
