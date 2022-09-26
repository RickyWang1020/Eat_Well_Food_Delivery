package com.abc.eatwell.service.impl;

import com.abc.eatwell.mapper.EmployeeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.eatwell.entity.Employee;
import com.abc.eatwell.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
