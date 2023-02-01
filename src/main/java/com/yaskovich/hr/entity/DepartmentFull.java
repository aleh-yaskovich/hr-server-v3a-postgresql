package com.yaskovich.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentFull extends DepartmentBase {
    private Integer numberOfEmployees;
    private String avgSalary;
}
