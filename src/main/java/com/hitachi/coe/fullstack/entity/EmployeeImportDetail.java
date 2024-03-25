package com.hitachi.coe.fullstack.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hitachi.coe.fullstack.entity.base.BaseAudit;
import com.hitachi.coe.fullstack.entity.base.BaseReadonlyEntity;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;
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
@Table(name = "employee_import_detail", schema = "public")
public class EmployeeImportDetail extends BaseAudit implements BaseReadonlyEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // its the each cell read from excel
    @Type(type = "jsonb")
    @Column(name = "body", columnDefinition = "jsonb")
    private Object body;

    @Enumerated(EnumType.ORDINAL)
    private ImportOperationStatus status;

    @Column(name = "line_num")
    private Integer lineNum;

    // preferred: List<ErrorLineModel>
    @Type(type = "jsonb")
    @Column(name = "message_line_list", columnDefinition = "jsonb")
    private Object messageLineList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_import_id")
    private EmployeeImport employeeImport;
}
