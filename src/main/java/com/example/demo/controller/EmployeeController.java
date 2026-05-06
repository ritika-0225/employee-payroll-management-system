package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Employee;
import com.example.demo.model.User;
import com.example.demo.service.EmployeeService;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.UserRepository;

@RestController
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private UserRepository userRepo;

    // ================= ADD EMPLOYEE =================

    @PostMapping("/addEmployee")
    public Employee addEmployee(@RequestBody Employee emp) {

        Employee savedEmp = repo.save(emp);

        String username = "emp" + savedEmp.getId();

        User user = new User();
        user.setUsername(username);
        user.setPassword("1234");
        user.setRole("Employee");
        user.setEmpId(savedEmp.getId());

        userRepo.save(user);

        return savedEmp;
    }

    // ================= GET EMPLOYEES =================

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return service.getAllEmployees();
    }

    // ================= UPDATE EMPLOYEE =================

    @PutMapping("/updateEmployee/{id}")
    public Employee updateEmployee(@PathVariable int id, @RequestBody Employee emp) {

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