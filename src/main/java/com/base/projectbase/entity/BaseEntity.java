package com.base.projectbase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass // help to inheritance field
public class BaseEntity implements Serializable {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false)
    @CreationTimestamp // for auto insert when entity saved
    private Date created;
}
