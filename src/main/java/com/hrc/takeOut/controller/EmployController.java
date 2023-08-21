package com.hrc.takeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrc.takeOut.core.commom.Result;
import com.hrc.takeOut.entity.Employee;
import com.hrc.takeOut.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployController {
    @Resource
    EmployeeService employeeService;

    /**
     * 员工登录
     */
    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        //多页面传来的用户密码进行md5加密
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        //创造查询条件--使用用户名进行查询
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Employee> eq = employeeLambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        //查询
        log.info("开始员工查询");
        Employee emp = employeeService.getOne(eq);
        log.info("结束员工查询");
        //判断是否登录成功
        if (emp == null) return Result.error("用户名错误");
        if (!emp.getPassword().equals(password)) return Result.error("密码错误");
        if (emp.getStatus() == 0) return Result.error("账号已锁定，请联系管理员解锁");
        //登录成功，将员工Id存入Session中
        /*
          错误❌*
          request.getSessionK().getAttribute("employee",employee.getId());
          这里的employee指的是接收前端参数的实体类  前端只传来了username与password 未传来id 所以不能使用employee
          emp指的是从数据库查询的员工实体类，包含员工一切信息
         */
        request.getSession().setAttribute("employee", emp.getId());
        return Result.success(emp);
    }

    /**
     * 员工退出
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        //删除session中保存的员工id
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 添加员工
     */
    @PostMapping
    public Result<String> save(HttpServletRequest ignoredRequest, @RequestBody Employee employee) {
        //设置初始密码为123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
       /*         设置创造时间和更新时间
                employee.setCreateTime(LocalDateTime.now());
                employee.setUpdateTime(LocalDateTime.now());
                设置添加员工的人和更新员工信息的人
                Long id = (Long) request.getSession().getAttribute("employee");
                employee.setCreateUser(id);
                employee.setUpdateUser(id);
        在数据库中保存新增员工信息*/
        employeeService.save(employee);
        return Result.success("员工添加成功");
    }

    /**
     * 员工信息分页查询
     */
    @GetMapping("/page")
    public Result<Page<Employee>> page(int page, int pageSize, String name) {
        log.info("/employee/page:{}, pageSize:{}, name:{}", page, pageSize, name);
        //构造分页查询器
        Page<Employee> employeePage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //模糊查询name
        employeeLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //将查询的结果按照更新时间降序
        employeeLambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        //查询
        log.info("开始员工信息分页查询");
        employeeService.page(employeePage, employeeLambdaQueryWrapper);
        log.info("结束员工信息分页查询");
        return Result.success(employeePage);
    }

    /**
     * 根据id修改员工信息
     */
    @PutMapping
    public Result<String> update(HttpServletRequest ignoredRequest, @RequestBody Employee employee) {

          /*  使用mp的全局处理字段进行赋值
          //修改员工信息更新时间
        //        employee.setUpdateTime(LocalDateTime.now());
                //更换修改员工信息的人
        //        Long id = (Long) request.getSession().getAttribute("employee");
        //        employee.setUpdateUser(id);
                //根据用户id更新数据库信息*/
        //将登录员工id存入ThreadLocal中给公共字段自动赋值
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    /* 根据id查询员工信息*/
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) return Result.success(employee);
        else return Result.error("根据id查询员工失败");

    }
}
