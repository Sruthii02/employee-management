package com.example.ems.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepartmentResponse {
    private Long id;
    private String name;
    private LocalDate creationDate;

    private List<EmployeeResponse> employees;

    private SimpleEmployeeDto departmentHead;
}
