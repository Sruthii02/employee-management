package com.example.ems.service;

import com.example.ems.dto.*;
import com.example.ems.entity.Department;
import com.example.ems.entity.Employee;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.repository.DepartmentRepository;
import com.example.ems.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Value("${app.pagination.default-size:20}")
    private int defaultSize;

    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest req) {

        Department dept = departmentRepository.findById(req.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id " + req.getDepartmentId()));

        if (req.getReportingManagerId() == null) {
            boolean isTopLevel = employeeRepository.count() == 0;
            if (!isTopLevel) {
                throw new IllegalArgumentException(
                        "Reporting manager is required for all employees except the top-level employee.");
            }
        } else {

            if (!employeeRepository.existsById(req.getReportingManagerId())) {
                throw new ResourceNotFoundException(
                        "Reporting manager not found with id " + req.getReportingManagerId());
            }
        }

        Employee e = new Employee();
        e.setName(req.getName());
        e.setDateOfBirth(req.getDateOfBirth());
        e.setSalary(req.getSalary());
        e.setDepartmentId(req.getDepartmentId());
        e.setAddress(req.getAddress());
        e.setRole(req.getRole());
        e.setJoiningDate(req.getJoiningDate());
        e.setYearlyBonus(req.getYearlyBonus());
        e.setReportingManagerId(req.getReportingManagerId());

        Employee saved = employeeRepository.save(e);

        return toResponse(saved);
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest req) {

        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        if (req.getName() != null)
            e.setName(req.getName());
        if (req.getDateOfBirth() != null)
            e.setDateOfBirth(req.getDateOfBirth());
        if (req.getSalary() != null)
            e.setSalary(req.getSalary());
        if (req.getAddress() != null)
            e.setAddress(req.getAddress());
        if (req.getRole() != null)
            e.setRole(req.getRole());
        if (req.getJoiningDate() != null)
            e.setJoiningDate(req.getJoiningDate());
        if (req.getYearlyBonus() != null)
            e.setYearlyBonus(req.getYearlyBonus());

        if (req.getDepartmentId() != null) {
            if (!departmentRepository.existsById(req.getDepartmentId())) {
                throw new ResourceNotFoundException(
                        "Department not found with id " + req.getDepartmentId());
            }
            e.setDepartmentId(req.getDepartmentId());
        }

        if (req.getReportingManagerId() != null) {

            if (Objects.equals(req.getReportingManagerId(), id)) {
                throw new IllegalArgumentException("Employee cannot be its own reporting manager");
            }

            if (e.getReportingManagerId() == null && req.getReportingManagerId() != null) {
                throw new IllegalArgumentException("Top-level employee cannot have a reporting manager.");
            }

            if (!employeeRepository.existsById(req.getReportingManagerId())) {
                throw new ResourceNotFoundException(
                        "Reporting manager not found with id " + req.getReportingManagerId());
            }

            if (isManagerCycle(id, req.getReportingManagerId())) {
                throw new IllegalArgumentException("Invalid hierarchy: circular manager reference detected");
            }

            e.setReportingManagerId(req.getReportingManagerId());
        }

        Employee saved = employeeRepository.save(e);

        return toResponse(saved);
    }

    private boolean isManagerCycle(Long employeeId, Long managerId) {
        while (managerId != null) {
            if (managerId.equals(employeeId)) {
                return true;
            }
            managerId = employeeRepository.findById(managerId)
                    .map(Employee::getReportingManagerId)
                    .orElse(null);
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> listEmployees(int page, int size) {
        if (size <= 0)
            size = defaultSize;
        PageRequest pr = PageRequest.of(Math.max(0, page), size, Sort.by("id").ascending());
        Page<Employee> p = employeeRepository.findAll(pr);

        List<EmployeeResponse> data = p.getContent().stream().map(this::toResponse).collect(Collectors.toList());
        return Map.of(
                "page", p.getNumber(),
                "size", p.getSize(),
                "totalItems", p.getTotalElements(),
                "totalPages", p.getTotalPages(),
                "data", data);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> lookupEmployees() {
        return employeeRepository.findAll().stream()
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", e.getId());
                    m.put("name", e.getName());
                    return m;
                })
                .collect(Collectors.toList());
    }

    private EmployeeResponse toResponse(Employee e) {
        EmployeeResponse r = new EmployeeResponse();
        r.setId(e.getId());
        r.setName(e.getName());
        r.setDateOfBirth(e.getDateOfBirth());
        r.setSalary(e.getSalary());
        r.setAddress(e.getAddress());
        r.setRole(e.getRole());
        r.setJoiningDate(e.getJoiningDate());
        r.setYearlyBonus(e.getYearlyBonus());

        if (e.getDepartmentId() != null) {
            SimpleDepartmentDto sd = new SimpleDepartmentDto();
            sd.setId(e.getDepartmentId());
            sd.setName(departmentRepository.findById(e.getDepartmentId())
                    .map(Department::getName)
                    .orElse(null));
            r.setDepartment(sd);
        }

        if (e.getReportingManagerId() != null) {
            SimpleManagerDto sm = new SimpleManagerDto();
            sm.setId(e.getReportingManagerId());
            sm.setName(employeeRepository.findById(e.getReportingManagerId())
                    .map(Employee::getName)
                    .orElse(null));
            r.setReportingManager(sm);
        }
        return r;
    }

    public EmployeeResponse updateEmployeeDepartment(Long employeeId, Long newDepartmentId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id " + employeeId));

        Department department = departmentRepository.findById(newDepartmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id " + newDepartmentId));

        employee.setDepartmentId(newDepartmentId);
        Employee updatedEmployee = employeeRepository.save(employee);

        return toResponse(updatedEmployee);
    }
}
