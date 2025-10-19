package com.example.ems.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateEmployeeRequest {
    @NotBlank
    private String name;
    private LocalDate dateOfBirth;
    private Double salary;
    private Long departmentId;
    private String address;
    private String role;
    private LocalDate joiningDate;
    private Double yearlyBonus;
    private Long reportingManagerId;
}
