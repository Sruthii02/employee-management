package com.example.ems.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate dateOfBirth;
    private Double salary;
    private String address;
    private String role;
    private LocalDate joiningDate;
    private Double yearlyBonus;

    // Department reference by ID only
    @Column(name = "department_id")
    private Long departmentId;

    // Reporting manager reference by ID only
    @Column(name = "reporting_manager_id")
    private Long reportingManagerId;

}
