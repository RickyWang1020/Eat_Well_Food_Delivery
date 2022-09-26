package com.abc.eatwell.controller;

import com.abc.eatwell.common.R;
import com.abc.eatwell.entity.Employee;
import com.abc.eatwell.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}
