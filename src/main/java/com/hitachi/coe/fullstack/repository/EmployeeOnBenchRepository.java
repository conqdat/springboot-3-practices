package com.hitachi.coe.fullstack.repository;

import com.hitachi.coe.fullstack.entity.EmployeeOnBench;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface EmployeeOnBenchRepository extends JpaRepository<EmployeeOnBench, Integer> {
}
