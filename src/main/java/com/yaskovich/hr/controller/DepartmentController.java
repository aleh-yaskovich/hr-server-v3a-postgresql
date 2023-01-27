package com.yaskovich.hr.controller;

import com.yaskovich.hr.models.DepartmentBaseModel;
import com.yaskovich.hr.models.DepartmentFullModel;
import com.yaskovich.hr.models.DepartmentModel;
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
    public List<DepartmentFullModel> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public DepartmentModel getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping()
    public boolean createDepartment(@RequestBody DepartmentBaseModel model) {
        return departmentService.createDepartment(model);
    }

    @PutMapping()
    public boolean updateDepartment(@RequestBody DepartmentBaseModel model) {
        return departmentService.updateDepartment(model);
    }

    @DeleteMapping("/{id}")
    public Integer deleteDepartmentById(@PathVariable Long id) {
        return departmentService.deleteDepartmentById(id);
    }
}
