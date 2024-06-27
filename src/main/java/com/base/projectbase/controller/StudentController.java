package com.base.projectbase.controller;

import com.base.projectbase.model.Student;
import com.base.projectbase.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {
    @Autowired
    StudentRepo studentRepo;

    @GetMapping
    public List<Student> getAllStudents(
            @RequestParam(value = "min", defaultValue = "0") int min,
            @RequestParam(value = "max", defaultValue = "100") int max
    ){
        return studentRepo.findByAgeBetween(min, max);
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student){
        return studentRepo.save(student);
    }

    @GetMapping("/by-birthday-range")
    public List<Student> getStudentsByBirthdayRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return studentRepo.findByBirthdayBetween(startDate, endDate);
    }
}
