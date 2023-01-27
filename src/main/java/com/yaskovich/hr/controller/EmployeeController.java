package com.yaskovich.hr.controller;

import com.yaskovich.hr.models.EmployeeBaseModel;
import com.yaskovich.hr.models.EmployeeFullModel;
import com.yaskovich.hr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping()
    public List<EmployeeBaseModel> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeBaseModel getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping()
    public boolean createEmployee(@RequestBody EmployeeFullModel model) {
        return employeeService.createEmployee(model);
    }

    @PutMapping()
    public boolean updateEmployee(@RequestBody EmployeeFullModel model) {
        return employeeService.updateEmployee(model);
    }

    @DeleteMapping("/{id}")
    public Integer deleteEmployeeById(@PathVariable Long id) {
        return employeeService.deleteEmployeeById(id);
    }
}
