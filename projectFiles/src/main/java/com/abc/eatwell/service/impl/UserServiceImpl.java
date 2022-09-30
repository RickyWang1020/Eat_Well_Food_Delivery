package com.abc.eatwell.service.impl;

import com.abc.eatwell.entity.User;
import com.abc.eatwell.mapper.UserMapper;
import com.abc.eatwell.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
