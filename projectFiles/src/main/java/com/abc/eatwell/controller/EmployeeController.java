package com.abc.eatwell.controller;

import com.abc.eatwell.common.R;
import com.abc.eatwell.entity.Employee;
import com.abc.eatwell.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Employee login
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        // 1. md5 encrypt the password submitted by the webpage
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2. query in the database based on the username
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 3. if no result, then return the result of login failed
        if (emp == null) {
            return R.error("login failed");
        }

        // 4. compare the password, if not match then return the result of login failed
        if (!emp.getPassword().equals(password)) {
            return R.error("login failed");
        }

        // 5. check employee status, if the status is "banned" then return the result of employee banned
        if (emp.getStatus() == 0) {
            return R.error("the account is banned");
        }

        // 6. successful login, store the employee id into the Session and return the result of login success
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * employee logout
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // clear the employee's id from Session
        request.getSession().removeAttribute("employee");
        return R.success("logout success");
    }

    /**
     * Add new employee
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        // add default password (123456) to employee
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        // set default create time, update time
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        // set the create user, update user as the current user
//        Long curEmployeeId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(curEmployeeId);
//        employee.setUpdateUser(curEmployeeId);

        employeeService.save(employee);
        return R.success("add new employee success");
    }

    /**
     * search for employee information in page segmentation
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        // construct page segmentation constructor
        Page<Employee> pageInfo = new Page(page, pageSize);

        // construct condition constructor
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // add conditions
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // add sort condition
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * change employee information based on id
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {

        Long curEmployeeId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(curEmployeeId);
        employeeService.updateById(employee);

        return R.success("update employee info success");
    }

    /**
     * get employee information by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
