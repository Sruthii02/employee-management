package com.example.ems.controller;

import com.example.ems.dto.CreateDepartmentRequest;
import com.example.ems.dto.DepartmentResponse;
import com.example.ems.dto.UpdateDepartmentRequest;
import com.example.ems.service.DepartmentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody CreateDepartmentRequest req) {
        return ResponseEntity.ok(departmentService.createDepartment(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentRequest req) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(Map.of("message", "Department deleted"));
    }

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String expand) {
        boolean expandEmployees = "employee".equalsIgnoreCase(expand);
        int s = (size == null) ? -1 : size;
        Map<String, Object> res = departmentService.listDepartments(page, s, expandEmployees);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getOne(@PathVariable Long id,
            @RequestParam(required = false) String expand) {
        boolean expandEmployees = "employee".equalsIgnoreCase(expand);
        return ResponseEntity.ok(departmentService.getDepartment(id, expandEmployees));
    }
}
