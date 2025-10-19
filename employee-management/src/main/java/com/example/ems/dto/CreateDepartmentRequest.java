package com.example.ems.dto;

import java.time.LocalDate;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateDepartmentRequest {
    @NotBlank
    private String name;

    @NotNull
    private LocalDate creationDate;

    // optional at create; must reference existing employee id if provided
    private Long departmentHeadId;
}
