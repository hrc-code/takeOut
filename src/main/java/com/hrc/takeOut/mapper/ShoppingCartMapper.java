package com.hrc.takeOut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrc.takeOut.model.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
