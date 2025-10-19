package com.example.ems.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ems.dto.CreateEmployeeRequest;
import com.example.ems.dto.EmployeeResponse;
import com.example.ems.dto.UpdateEmployeeRequest;
import com.example.ems.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody CreateEmployeeRequest req) {
        EmployeeResponse created = employeeService.createEmployee(req);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest req) {
        EmployeeResponse updated = employeeService.updateEmployee(id, req);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean lookup) {

        if (lookup != null && lookup) {
            List<Map<String, Object>> lookupList = employeeService.lookupEmployees();
            return ResponseEntity.ok(lookupList);
        }
        int s = (size == null) ? -1 : size;
        Map<String, Object> res = employeeService.listEmployees(page, s);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}/department")
    public ResponseEntity<EmployeeResponse> moveEmployeeToDepartment(
            @PathVariable("id") Long employeeId,
            @RequestParam("departmentId") Long departmentId) {

        EmployeeResponse updatedEmployee = employeeService.updateEmployeeDepartment(employeeId, departmentId);
        return ResponseEntity.ok(updatedEmployee);
    }
}
