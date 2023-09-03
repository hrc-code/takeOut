package com.hrc.takeOut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrc.takeOut.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
