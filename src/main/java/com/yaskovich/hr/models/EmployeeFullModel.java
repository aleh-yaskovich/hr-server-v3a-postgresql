package com.yaskovich.hr.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFullModel extends EmployeeBaseModel {
    private String email;
    private int department;
    private String position;
    private String summary;
}
