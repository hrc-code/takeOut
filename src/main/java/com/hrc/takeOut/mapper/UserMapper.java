package com.hrc.takeOut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrc.takeOut.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
