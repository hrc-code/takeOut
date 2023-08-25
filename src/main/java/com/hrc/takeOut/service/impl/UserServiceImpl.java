package com.hrc.takeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrc.takeOut.entity.User;
import com.hrc.takeOut.mapper.UserMapper;
import com.hrc.takeOut.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
