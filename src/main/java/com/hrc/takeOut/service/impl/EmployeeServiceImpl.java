package com.hrc.takeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrc.takeOut.entity.Employee;
import com.hrc.takeOut.mapper.EmployeeMapper;
import com.hrc.takeOut.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{

}
