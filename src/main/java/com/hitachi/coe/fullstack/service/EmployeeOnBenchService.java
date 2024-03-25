package com.hitachi.coe.fullstack.service;

import com.hitachi.coe.fullstack.model.EmployeeOnBenchModel;
import org.springframework.data.domain.Page;

public interface EmployeeOnBenchService {
    /**
     * Get all employee on bench.
     *
     * @param no     The page number to retrieve.
     * @param limit  The maximum employee of results to return of each page.
     * @param sortBy The field to sort the results by.
     * @param desc   The field to sort the results asc or desc.
     * @return A page of employee on bench models.
     * @author ThuyTrinhThanhLe
     */
    Page<EmployeeOnBenchModel> getAllEmployeeOnBench(Integer no, Integer limit, String sortBy, Boolean desc);
}
