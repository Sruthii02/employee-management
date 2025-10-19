package com.example.ems.service;

import com.example.ems.dto.*;
import com.example.ems.entity.Department;
import com.example.ems.entity.Employee;
import com.example.ems.exception.DepartmentDeletionException;
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
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Value("${app.pagination.default-size:20}")
    private int defaultSize;

    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest req) {

        Department dept = new Department();
        dept.setName(req.getName());
        dept.setCreationDate(req.getCreationDate());

        if (req.getDepartmentHeadId() != null) {
            Employee head = employeeRepository.findById(req.getDepartmentHeadId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Employee not found with id " + req.getDepartmentHeadId()));
            dept.setDepartmentHeadId(head.getId());
        } else {
            dept.setDepartmentHeadId(null);
        }

        Department saved = departmentRepository.save(dept);

        DepartmentResponse response = new DepartmentResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setCreationDate(saved.getCreationDate());

        if (saved.getDepartmentHeadId() != null) {
            employeeRepository.findById(saved.getDepartmentHeadId()).ifPresent(head -> {
                SimpleEmployeeDto headDto = new SimpleEmployeeDto();
                headDto.setId(head.getId());
                headDto.setName(head.getName());
                response.setDepartmentHead(headDto);
            });
        }

        return response;
    }

    public DepartmentResponse updateDepartment(Long departmentId, UpdateDepartmentRequest req) {

        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id " + departmentId));

        if (req.getName() != null) {
            dept.setName(req.getName());
        }

        if (req.getCreationDate() != null) {
            dept.setCreationDate(req.getCreationDate());
        }

        if (req.getDepartmentHeadId() != null) {
            Employee head = employeeRepository.findById(req.getDepartmentHeadId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Employee not found with id " + req.getDepartmentHeadId()));
            System.out.println("head id: " + head.getId());

            dept.setDepartmentHeadId(head.getId());
        }

        departmentRepository.save(dept);

        return toResponse(dept);
    }

    private DepartmentResponse toResponse(Department dept) {
        DepartmentResponse resp = new DepartmentResponse();
        resp.setId(dept.getId());
        resp.setName(dept.getName());
        resp.setCreationDate(dept.getCreationDate());

        if (dept.getDepartmentHeadId() != null) {
            employeeRepository.findById(dept.getDepartmentHeadId()).ifPresent(head -> {
                SimpleEmployeeDto headDto = new SimpleEmployeeDto();
                headDto.setId(head.getId());
                headDto.setName(head.getName());
                resp.setDepartmentHead(headDto);
            });
        }

        List<EmployeeResponse> employees = employeeRepository.findByDepartmentId(dept.getId())
                .stream()
                .map(emp -> {
                    EmployeeResponse er = new EmployeeResponse();
                    er.setId(emp.getId());
                    er.setName(emp.getName());
                    er.setDateOfBirth(emp.getDateOfBirth());
                    er.setSalary(emp.getSalary());
                    er.setAddress(emp.getAddress());
                    er.setRole(emp.getRole());
                    er.setJoiningDate(emp.getJoiningDate());
                    er.setYearlyBonus(emp.getYearlyBonus());

                    if (emp.getReportingManagerId() != null) {
                        employeeRepository.findById(emp.getReportingManagerId()).ifPresent(mgr -> {
                            SimpleManagerDto sm = new SimpleManagerDto();
                            sm.setId(mgr.getId());
                            sm.setName(mgr.getName());
                            er.setReportingManager(sm);
                        });
                    }

                    if (emp.getDepartmentId() != null) {
                        SimpleDepartmentDto sd = new SimpleDepartmentDto();
                        sd.setId(emp.getDepartmentId());
                        sd.setName(departmentRepository.findById(emp.getDepartmentId())
                                .map(Department::getName)
                                .orElse(null));
                        er.setDepartment(sd);
                    }

                    return er;
                })
                .collect(Collectors.toList());

        resp.setEmployees(employees);

        return resp;
    }

    public void deleteDepartment(Long id) {
        if (employeeRepository.existsByDepartmentId(id)) {
            throw new DepartmentDeletionException("Cannot delete department: employees assigned to this department");
        }
        departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
        departmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> listDepartments(int page, int size, boolean expandEmployees) {
        if (size <= 0)
            size = defaultSize;
        PageRequest pr = PageRequest.of(Math.max(0, page), size, Sort.by("id").ascending());
        Page<Department> p = departmentRepository.findAll(pr);

        List<DepartmentResponse> data = p.getContent().stream()
                .map(d -> toResponse(d, expandEmployees))
                .collect(Collectors.toList());

        return Map.of(
                "page", p.getNumber(),
                "size", p.getSize(),
                "totalItems", p.getTotalElements(),
                "totalPages", p.getTotalPages(),
                "data", data);
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartment(Long id, boolean expandEmployees) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
        return toResponse(d, expandEmployees);
    }

    private DepartmentResponse toResponse(Department dept, boolean expandEmployees) {
        DepartmentResponse resp = new DepartmentResponse();
        resp.setId(dept.getId());
        resp.setName(dept.getName());
        resp.setCreationDate(dept.getCreationDate());

        if (dept.getDepartmentHeadId() != null) {
            employeeRepository.findById(dept.getDepartmentHeadId()).ifPresent(head -> {
                SimpleEmployeeDto headDto = new SimpleEmployeeDto();
                headDto.setId(head.getId());
                headDto.setName(head.getName());
                resp.setDepartmentHead(headDto);
            });
        }

        if (expandEmployees) {
            List<EmployeeResponse> employees = employeeRepository.findByDepartmentId(dept.getId())
                    .stream()
                    .map(emp -> {
                        EmployeeResponse er = new EmployeeResponse();
                        er.setId(emp.getId());
                        er.setName(emp.getName());
                        er.setDateOfBirth(emp.getDateOfBirth());
                        er.setSalary(emp.getSalary());
                        er.setAddress(emp.getAddress());
                        er.setRole(emp.getRole());
                        er.setJoiningDate(emp.getJoiningDate());
                        er.setYearlyBonus(emp.getYearlyBonus());

                        if (emp.getReportingManagerId() != null) {
                            employeeRepository.findById(emp.getReportingManagerId()).ifPresent(mgr -> {
                                SimpleManagerDto sm = new SimpleManagerDto();
                                sm.setId(mgr.getId());
                                sm.setName(mgr.getName());
                                er.setReportingManager(sm);
                            });
                        }
                        return er;
                    })
                    .collect(Collectors.toList());
            resp.setEmployees(employees);
        }

        return resp;
    }
}
