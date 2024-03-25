package com.hitachi.coe.fullstack.repository;

import com.hitachi.coe.fullstack.entity.CoeUtilization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CoeUtilizationRepository extends JpaRepository<CoeUtilization,Integer> {

    /**
     * Retrieves a list of CoE Utilization that from start date to end date.
     *
     * @param fromDate start date of utilization time (optional)
     * @param toDate end date of utilization time (optional)
     * @return the list of CoE Utilization.
     * @author tquangpham
     */
    @Query(value =
            "SELECT cu from CoeUtilization cu " +
            "WHERE cu.startDate BETWEEN COALESCE(CAST(CAST(:fromDate as text) as timestamp), cu.startDate) " +
            "AND COALESCE(CAST(CAST(:toDate as text) as timestamp) , cu.startDate)")
    List<CoeUtilization> getListOfCoeUtilization(@Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate);
}
