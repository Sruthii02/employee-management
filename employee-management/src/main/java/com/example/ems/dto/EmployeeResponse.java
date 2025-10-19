package com.example.ems.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeResponse {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private Double salary;

    private String address;
    private String role;
    private LocalDate joiningDate;
    private Double yearlyBonus;

    private SimpleDepartmentDto department;
    private SimpleManagerDto reportingManager;

}
