package com.example.EmployeeManagementSystem.controllers;

import com.example.EmployeeManagementSystem.entities.Employee;
import com.example.EmployeeManagementSystem.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private final EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/employees")
    public List<Employee> getAll() {
        return service.getAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getById(@PathVariable("id") long id) {
        Employee employee = service.getById(id);
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@Valid @RequestBody Employee employee) {
        return service.create(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> update(@PathVariable("id") long id, @Valid @RequestBody Employee employeeDetails) {
        Employee updated = service.update(id, employeeDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/employees/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        service.delete(id);
    }
}
