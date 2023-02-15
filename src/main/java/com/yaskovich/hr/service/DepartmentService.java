package com.yaskovich.hr.service;

import com.yaskovich.hr.dto.DepartmentDto;
import com.yaskovich.hr.dto.EmployeeDto;
import com.yaskovich.hr.entity.DepartmentBase;
import com.yaskovich.hr.entity.DepartmentFull;
import com.yaskovich.hr.entity.Department;
import com.yaskovich.hr.entity.EmployeeBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentDto departmentDto;
    @Autowired
    private EmployeeDto employeeDto;

    public List<DepartmentFull> getAllDepartments() {
        return departmentDto.getAllDepartments();
    }

    public Department getDepartmentById(Long id) {
        Optional<DepartmentFull> optional = departmentDto.getDepartmentById(id);
        if(optional.isPresent()) {
            DepartmentFull departmentFullModel = optional.get();
            List<EmployeeBase> employees = employeeDto.getEmployeesByDepartment(departmentFullModel.getTitle());
            return Department.builder()
                    .id(departmentFullModel.getId())
                    .title(departmentFullModel.getTitle())
                    .numberOfEmployees(departmentFullModel.getNumberOfEmployees())
                    .avgSalary(departmentFullModel.getAvgSalary())
                    .employees(employees)
                    .build();
        } else {
            throw new RuntimeException("Department with id "+id+" not found");
        }
    }

    public boolean createDepartment(DepartmentBase model) {
        return departmentDto.createDepartment(model);
    }

    public boolean updateDepartment(DepartmentBase model) {
        return departmentDto.updateDepartment(model);
    }

    public Integer deleteDepartmentById(Long id) {
        return departmentDto.deleteDepartmentById(id);
    }
}
