package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.*;
import com.example.demo.repository.*;

@RestController
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private EmployeeRepository empRepo;

    // ================= CHECK-IN =================

    @PostMapping("/markAttendance")
    public Map<String, Object> markAttendance(@RequestBody Map<String, Object> data) {

        Map<String, Object> response = new HashMap<>();

        try {

            int empId = Integer.parseInt(data.get("empId").toString());

            Employee emp = empRepo.findById(empId).orElse(null);
            if (emp == null) {
                response.put("status","error");
                response.put("message","Employee not found");
                return response;
            }

            LocalDate today = LocalDate.now();

            Optional<Attendance> existing =
                    attendanceRepo.findByEmployeeIdAndDate(empId, today);

            if (existing.isPresent()) {
                response.put("status","error");
                response.put("message","Already checked in today");
                return response;
            }

            Attendance att = new Attendance();

            att.setEmployee(emp);
            att.setStatus("Present");

            att.setLat(Double.parseDouble(data.get("lat").toString()));
            att.setLng(Double.parseDouble(data.get("lng").toString()));

            att.setDate(today);
            att.setCheckinTime(LocalDateTime.now());

            attendanceRepo.save(att);

            response.put("status","success");
            response.put("checkinTime", att.getCheckinTime());

        } catch (Exception e) {

            response.put("status","error");
            response.put("message", e.getMessage());

        }

        return response;
    }

    // ================= CHECK-OUT =================

    @PostMapping("/checkout")
    public Map<String,Object> checkout(@RequestParam int empId){

        Map<String,Object> response = new HashMap<>();

        try{

            LocalDate today = LocalDate.now();

            Optional<Attendance> optional =
                    attendanceRepo.findByEmployeeIdAndDate(empId,today);

            if(optional.isEmpty()){
                response.put("message","Please check-in first");
                return response;
            }

            Attendance att = optional.get();

            if(att.getCheckoutTime()!=null){
                response.put("message","Already checked out");
                return response;
            }

            att.setCheckoutTime(LocalDateTime.now());

            attendanceRepo.save(att);

            response.put("time",att.getCheckoutTime());

        }
        catch(Exception e){
            response.put("message","Server error");
        }

        return response;
    }

    // ================= HISTORY =================

    @GetMapping("/getAttendance")
    public List<Map<String,Object>> getAttendance(){

        List<Attendance> list = attendanceRepo.findAll();

        List<Map<String,Object>> result = new ArrayList<>();

        for(Attendance a : list){

            Map<String,Object> map = new HashMap<>();

            map.put("empId",a.getEmployee().getId());
            map.put("status",a.getStatus());
            map.put("lat",a.getLat());
            map.put("lng",a.getLng());
            map.put("checkinTime",a.getCheckinTime());
            map.put("checkoutTime",a.getCheckoutTime());
            map.put("date",a.getDate());

            result.add(map);
        }

        return result;
    }

}