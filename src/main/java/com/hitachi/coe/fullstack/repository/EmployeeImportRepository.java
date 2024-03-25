package com.hitachi.coe.fullstack.repository;

import com.hitachi.coe.fullstack.entity.EmployeeImport;
import com.hitachi.coe.fullstack.model.IEmployeeImportSearch;
import com.hitachi.coe.fullstack.model.ImportOperationStatus;
import com.hitachi.coe.fullstack.model.ImportOperationType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeImportRepository extends JpaRepository<EmployeeImport, Integer> {

        /**
         * Search employee import by keyword, type, status.
         * 
         * @param keyword  search keyword
         * @param type     import type of {@link Integer}
         * @param status   import status of {@link Integer}
         * @param pageable {@link Pageable} for pagination
         * @return a {@link Page} of {@link IEmployeeImportSearch}
         * @author tminhto
         * 
         * @see ImportOperationType
         * @see ImportOperationStatus
         */
        @Query(value = "SELECT empImp.id AS id, " +
                        "   empImp.name AS name, " +
                        "   empImp.type AS type, " +
                        "   empImp.status AS status, " +
                        "   CAST(empImp.message AS TEXT) AS message, " +
                        "   empImp.created_date AS created, " +
                        "   empImp.created_by AS createdBy, " +
                        "   empImp.updated_date AS updated, " +
                        "   empImp.updated_by AS updatedBy " +
                        "FROM employee_import empImp " +
                        "WHERE ( :keyword IS NULL " +
                        "       OR empImp.name LIKE :keyword " +
                        "      ) " +
                        "AND ( :type IS NULL OR empImp.type = CAST(CAST(:type AS TEXT) AS INTEGER) ) " +
                        "AND ( :status IS NULL OR empImp.status = CAST(CAST(:status AS TEXT) AS INTEGER) ) ", nativeQuery = true)
        Page<IEmployeeImportSearch> search(@Param("keyword") String keyword,
                        @Param("type") Integer type,
                        @Param("status") Integer status,
                        Pageable pageable);
}