package com.hrc.takeOut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrc.takeOut.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
