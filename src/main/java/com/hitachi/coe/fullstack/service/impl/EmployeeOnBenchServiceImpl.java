package com.hitachi.coe.fullstack.service.impl;

import com.hitachi.coe.fullstack.model.EmployeeOnBenchModel;
import com.hitachi.coe.fullstack.repository.EmployeeOnBenchRepository;
import com.hitachi.coe.fullstack.service.EmployeeOnBenchService;
import com.hitachi.coe.fullstack.transformation.EmployeeOnBenchTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeOnBenchServiceImpl implements EmployeeOnBenchService {
    private final EmployeeOnBenchRepository employeeOnBenchRepository;

    @Autowired
    private EmployeeOnBenchTransformer employeeOnBenchTransformer;

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
    @Override
    public Page<EmployeeOnBenchModel> getAllEmployeeOnBench(Integer no, Integer limit, String sortBy, Boolean desc) {
        Sort sort = Sort.by(sortBy);
        if (desc != null) {
            sort = sort.descending();
        }
        final Pageable pageable = PageRequest.of(no, limit, sort);

        return employeeOnBenchRepository.findAll(pageable).map(p -> employeeOnBenchTransformer.apply(p));
    }
}
