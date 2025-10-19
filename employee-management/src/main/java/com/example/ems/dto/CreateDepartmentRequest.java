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

    private Long departmentHeadId;
}
