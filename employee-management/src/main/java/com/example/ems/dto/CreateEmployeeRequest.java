package com.example.ems.dto;

import java.time.LocalDate;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateEmployeeRequest {
    @NotBlank
    private String name;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    @DecimalMin("0.0")
    private Double salary;

    @NotNull
    private Long departmentId;

    @NotBlank
    private String address;

    @NotBlank
    private String role;

    @NotNull
    private LocalDate joiningDate;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Double yearlyBonus;

    private Long reportingManagerId;
}
