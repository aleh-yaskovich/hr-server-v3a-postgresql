package com.yaskovich.hr.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentFullModel extends DepartmentBaseModel {
    private Integer numberOfEmployees;
    private Double avgSalary;
}
