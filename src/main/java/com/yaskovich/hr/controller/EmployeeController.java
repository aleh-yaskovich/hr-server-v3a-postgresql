package com.yaskovich.hr.controller;

import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.EmployeeBaseRequestModel;
import com.yaskovich.hr.controller.model.EmployeeFullRequestModel;
import com.yaskovich.hr.entity.EmployeeBase;
import com.yaskovich.hr.entity.EmployeeFull;
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
    public List<EmployeeBase> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeFullRequestModel getEmployeeById(@PathVariable Long id) {
        try {
            EmployeeFull employeeFull = employeeService.getEmployeeById(id);
            return EmployeeFullRequestModel.builder()
                    .employeeFull(employeeFull)
                    .status(BaseModel.Status.SUCCESS)
                    .build();
        } catch(Exception e) {
            return EmployeeFullRequestModel.builder()
                    .status(BaseModel.Status.FAILURE)
                    .message(e.getMessage())
                    .build();
        }
    }

    @PostMapping()
    public BaseModel createEmployee(@RequestBody EmployeeFull model) {
        try {
            employeeService.createEmployee(model);
            return BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        } catch(Exception e) {
            return BaseModel.builder()
                    .status(BaseModel.Status.FAILURE)
                    .message(e.getMessage())
                    .build();
        }
    }

    @PutMapping()
    public BaseModel updateEmployee(@RequestBody EmployeeFull model) {
        try {
            employeeService.updateEmployee(model);
            return BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        } catch(Exception e) {
            return BaseModel.builder()
                    .status(BaseModel.Status.FAILURE)
                    .message(e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public BaseModel deleteEmployeeById(@PathVariable Long id) {
        if(employeeService.deleteEmployeeById(id) == 1) {
            return BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        } else {
            return BaseModel.builder().status(BaseModel.Status.FAILURE).build();
        }
    }
}
