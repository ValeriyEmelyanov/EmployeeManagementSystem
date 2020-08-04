package com.example.EmployeeManagementSystem.services;

import com.example.EmployeeManagementSystem.entities.Employee;
import com.example.EmployeeManagementSystem.exceptions.ResourceNotFoundException;
import com.example.EmployeeManagementSystem.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private static final String EMPLOYEE_NOT_FOUND_FOR_THIS_ID = "Employee not found for this id : ";

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND_FOR_THIS_ID + id));
    }

    public Employee create(Employee employee) {
        return repository.save(employee);
    }

    public Employee update(long id, Employee employeeDetails) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND_FOR_THIS_ID + id));
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        return repository.save(employee);
    }

    public void delete(long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND_FOR_THIS_ID + id));
        repository.delete(employee);
    }
}
