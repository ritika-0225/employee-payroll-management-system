package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    // ✅ Add
    public Employee addEmployee(Employee emp) {
        return repo.save(emp);
    }

    // ✅ Get All
    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    // ✅ 🔥 Update (IMPORTANT)
    public Employee updateEmployee(int id, Employee emp) {

        Employee existing = repo.findById(id).orElse(null);

        if (existing != null) {
            existing.setName(emp.getName());
            existing.setDepartment(emp.getDepartment());
            existing.setSalary(emp.getSalary());
            existing.setEmail(emp.getEmail());

            return repo.save(existing);
        }

        return null;
    }
}