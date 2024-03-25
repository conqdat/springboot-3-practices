package com.hitachi.coe.fullstack.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hitachi.coe.fullstack.entity.base.BaseAudit;
import com.hitachi.coe.fullstack.entity.base.BaseReadonlyEntity;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;
import com.hitachi.coe.fullstack.model.ImportOperationType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Getter
@Setter
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Entity
@Table(name = "employee_import", schema = "public")
public class EmployeeImport extends BaseAudit implements BaseReadonlyEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // storing filename: "abcd.csv", "abcd.xlsx"
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private ImportOperationType type;

    @Enumerated(EnumType.ORDINAL)
    private ImportOperationStatus status;

    // preferred: List<ExcelErrorDetail>
    @Type(type = "jsonb")
    @Column(name = "message", columnDefinition = "jsonb")
    private Object message;

    @OneToMany(mappedBy = "employeeImport", fetch = FetchType.LAZY)
    private List<EmployeeImportDetail> employeeImportDetails;
}