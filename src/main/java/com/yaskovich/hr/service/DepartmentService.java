package com.yaskovich.hr.service;

import com.yaskovich.hr.dto.DepartmentDto;
import com.yaskovich.hr.dto.EmployeeDto;
import com.yaskovich.hr.models.DepartmentBaseModel;
import com.yaskovich.hr.models.DepartmentFullModel;
import com.yaskovich.hr.models.DepartmentModel;
import com.yaskovich.hr.models.EmployeeBaseModel;
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

    public List<DepartmentFullModel> getAllDepartments() {
        return departmentDto.getAllDepartments();
    }

    public DepartmentModel getDepartmentById(Long id) {
        Optional<DepartmentFullModel> optional = departmentDto.getDepartmentById(id);
        if(optional.isPresent()) {
            DepartmentFullModel departmentFullModel = optional.get();
            List<EmployeeBaseModel> employees = employeeDto.getEmployeesByDepartment(id);
            return DepartmentModel.builder()
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

    public boolean createDepartment(DepartmentBaseModel model) {
        return departmentDto.createDepartment(model);
    }

    public boolean updateDepartment(DepartmentBaseModel model) {
        return departmentDto.updateDepartment(model);
    }

    public Integer deleteDepartmentById(Long id) {
        return departmentDto.deleteDepartmentById(id);
    }
}
