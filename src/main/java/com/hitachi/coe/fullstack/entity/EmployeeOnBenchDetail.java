package com.hitachi.coe.fullstack.entity;

import com.hitachi.coe.fullstack.entity.base.BaseAudit;
import com.hitachi.coe.fullstack.entity.base.BaseReadonlyEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "employee_on_bench_detail", schema = "public")
public class EmployeeOnBenchDetail extends BaseAudit implements BaseReadonlyEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bench_days")
    private Integer benchDays;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_of_join")
    private Date dateOfJoin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "status_change_date")
    private Date statusChangeDate;

    @Column(name = "category_code")
    private Integer categoryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_on_bench_id")
    private EmployeeOnBench employeeOnBench;

}
