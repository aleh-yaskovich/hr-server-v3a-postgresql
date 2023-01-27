package com.yaskovich.hr.service;

import com.yaskovich.hr.dto.EmployeeDto;
import com.yaskovich.hr.models.EmployeeBaseModel;
import com.yaskovich.hr.models.EmployeeFullModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeDto employeeDto;

    public List<EmployeeBaseModel> getAllEmployees() {
        return employeeDto.getAllEmployees();
    }

    public EmployeeFullModel getEmployeeById(Long id) {
        Optional<EmployeeFullModel> optional = employeeDto.getEmployeeById(id);
        if(optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Employee with ID "+id+" not found");
        }
    }

    public boolean createEmployee(EmployeeFullModel model) {
        return employeeDto.createEmployee(model);
    }

    public boolean updateEmployee(EmployeeFullModel model) {
        return employeeDto.updateEmployee(model);
    }

    public Integer deleteEmployeeById(Long id) {
        return employeeDto.deleteEmployeeById(id);
    }
}
