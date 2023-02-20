package com.yaskovich.hr.controller;

import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.DepartmentRequestModel;
import com.yaskovich.hr.entity.DepartmentBase;
import com.yaskovich.hr.entity.DepartmentFull;
import com.yaskovich.hr.entity.Department;
import com.yaskovich.hr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping()
    public List<DepartmentFull> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public DepartmentRequestModel getDepartmentById(@PathVariable Long id) {
        try {
            Department department = departmentService.getDepartmentById(id);
            return DepartmentRequestModel.builder()
                    .department(department)
                    .status(BaseModel.Status.SUCCESS)
                    .build();
        } catch (Exception e) {
            return DepartmentRequestModel.builder()
                    .status(BaseModel.Status.FAILURE)
                    .message(e.getMessage())
                    .build();
        }
    }

    @PostMapping()
    public BaseModel createDepartment(@RequestBody DepartmentBase model) {
        try {
            departmentService.createDepartment(model);
            return BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        } catch(Exception e) {
            return BaseModel.builder()
                    .status(BaseModel.Status.FAILURE)
                    .message(e.getMessage())
                    .build();
        }
    }

    @PutMapping()
    public BaseModel updateDepartment(@RequestBody DepartmentBase model) {
        try {
            departmentService.updateDepartment(model);
            return BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        } catch(Exception e) {
            return BaseModel.builder()
                    .status(BaseModel.Status.FAILURE)
                    .message(e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public BaseModel deleteDepartmentById(@PathVariable Long id) {
        if(departmentService.deleteDepartmentById(id) == 1) {
            return BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        } else {
            return BaseModel.builder().status(BaseModel.Status.FAILURE).build();
        }
    }
}