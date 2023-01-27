package com.yaskovich.hr.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBaseModel {
    private Long id;
    private String firstName;
    private String lastName;
    private Double salary;
    private Date hiring;
}
