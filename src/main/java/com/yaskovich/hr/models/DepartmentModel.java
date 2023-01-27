package com.yaskovich.hr.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentModel extends DepartmentFullModel{
    private List<EmployeeBaseModel> employees;
}
