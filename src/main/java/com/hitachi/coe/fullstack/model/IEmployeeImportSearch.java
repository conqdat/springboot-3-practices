package com.hitachi.coe.fullstack.model;

import java.util.Date;

import com.hitachi.coe.fullstack.repository.EmployeeImportRepository;

/**
 * The interface IEmployeeImportSearch is used to define search result of
 * employee import.
 * 
 * @see EmployeeImportRepository#search(String, Integer, Integer,
 *      org.springframework.data.domain.Pageable)
 */
public interface IEmployeeImportSearch {
    Integer getId();

    String getName();

    Integer getType();

    Integer getStatus();

    String getMessage();

    Date getCreated();

    String getCreatedBy();

    Date getUpdated();

    String getUpdatedBy();
}
