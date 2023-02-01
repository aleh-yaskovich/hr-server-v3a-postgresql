package com.yaskovich.hr.service;

import com.yaskovich.hr.dto.EmployeeDto;
import com.yaskovich.hr.entity.EmployeeBase;
import com.yaskovich.hr.entity.EmployeeFull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeDto employeeDto;

    public List<EmployeeBase> getAllEmployees() {
        return employeeDto.getAllEmployees();
    }

    public EmployeeFull getEmployeeById(Long id) {
        Optional<EmployeeFull> optional = employeeDto.getEmployeeById(id);
        if(optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Employee with ID "+id+" not found");
        }
    }

    public boolean createEmployee(EmployeeFull model) {
        return employeeDto.createEmployee(model);
    }

    public boolean updateEmployee(EmployeeFull model) {
        return employeeDto.updateEmployee(model);
    }

    public Integer deleteEmployeeById(Long id) {
        return employeeDto.deleteEmployeeById(id);
    }
}
