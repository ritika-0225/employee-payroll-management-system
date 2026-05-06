package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService service;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {

        User u = service.login(user.getUsername(), user.getPassword());

        Map<String, Object> response = new HashMap<>();

        if (u != null) {

            response.put("status", "success");
            response.put("role", u.getRole());
            response.put("username", u.getUsername());
            response.put("empId", u.getEmpId());   // ⭐ IMPORTANT FIX

        } else {

            response.put("status", "fail");

        }

        return response;
    }
}