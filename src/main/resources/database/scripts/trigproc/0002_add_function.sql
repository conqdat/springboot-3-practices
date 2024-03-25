-- FUNCTION: getEmployeeUtilizationNoUT(VARCHAR(50), INTEGER, VARCHAR(100), VARCHAR(50), VARCHAR(250), VARCHAR(250), VARCHAR(250), INTEGER)
-- Created by tquangpham
CREATE OR REPLACE FUNCTION getEmployeeUtilizationNoUT(
    billableThreshold double precision DEFAULT NULL::double precision,
    fromdate date DEFAULT NULL::date,
    todate date DEFAULT NULL::date)
RETURNS TABLE (
    hccId VARCHAR(50),
    id INTEGER,
    employeeName VARCHAR(100),
    email VARCHAR(50),
    branchName VARCHAR(250),
    businessName VARCHAR(250),
    teamName VARCHAR(250),
    daysFree INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT
    e.hcc_id,
    e.id,
    e.name AS employeeName,
    e.email,
    b.name AS branchName,
    bu.name AS businessName,
    t.name AS teamName,
    CAST(SUM(counts.num_days) AS INTEGER) AS daysFree
FROM
    employee e
JOIN
    branch b ON b.id = e.branch_id
JOIN
    business_unit bu ON bu.id = e.business_unit_id
JOIN
    coe_core_team t ON t.id = e.coe_core_team_id
JOIN
    coe_utilization cu ON cu.start_date BETWEEN COALESCE(fromdate, cu.start_date) AND COALESCE(todate, cu.start_date)
JOIN (
    SELECT
        date_trunc('month', end_date) AS month,
        MAX(end_date) AS max_date
    FROM
        coe_utilization
    WHERE
        start_date BETWEEN COALESCE(fromdate, start_date) AND COALESCE(todate, start_date)
    GROUP BY
        month
) coeUT ON coeUT.max_date = cu.end_date
CROSS JOIN LATERAL (
    SELECT
        COUNT(*) AS num_days
    FROM
        generate_series(cu.start_date::date, cu.end_date::date, '1 day') dates
    WHERE
        extract(DOW FROM dates) NOT IN (0, 6)
) counts
WHERE
    NOT EXISTS (
        SELECT
            1
        FROM
            employee_utilization eu
        WHERE
            eu.employee_id = e.id
    )
    AND cu.id IN (
        SELECT
            coeut2.id
        FROM
            coe_utilization coeut2
        JOIN (
            SELECT
                coeut1.end_date,
                MAX(coeut1.created_date) AS lasted_date
            FROM
                coe_utilization coeut1
            WHERE
                coeut1.end_date = cu.end_date
            GROUP BY
                coeut1.end_date
        ) coeLasted ON coeLasted.end_date = coeut2.end_date
            AND coeLasted.lasted_date = coeut2.created_date
    )
    AND e.id IN (
        SELECT
            es.employee_id
        FROM
            employee_status es
        WHERE
            es.status IN (1,2)
            AND es.employee_id = e.id
        GROUP BY
            es.employee_id
        HAVING
            MAX(es.status_date) = (
                SELECT
                    MAX(es2.status_date)
                FROM
                    employee_status es2
                WHERE
                    es2.employee_id = es.employee_id
            )
    )
GROUP BY
    e.hcc_id,
    e.id,
    e.name,
    e.email,
    b.name,
    bu.name,
    t.name

UNION ALL

SELECT
    em.hcc_id,
    em.id,
    em.name AS employeeName,
    em.email,
    b.name AS branchName,
    bu.name AS businessName,
    t.name AS teamName,
    CAST(emp_free.daysFree AS INTEGER) AS daysFree
FROM
    employee em
JOIN
    branch b ON b.id = em.branch_id
JOIN
    business_unit bu ON bu.id = em.business_unit_id
JOIN
    coe_core_team t ON t.id = em.coe_core_team_id
JOIN
    (
    SELECT
        e.id, CAST((SUM(eu.available_hours) - SUM(eu.billable_hours))/8 AS INTEGER) AS daysFree
    FROM
        employee e
    JOIN
        employee_utilization eu ON eu.employee_id = e.id
    JOIN
        coe_utilization cu ON cu.id = eu.coe_utilization_id
    JOIN
        (
        SELECT
            date_trunc('month', end_date) AS month,
            MAX(end_date) AS max_date
        FROM
            coe_utilization
        WHERE
            start_date BETWEEN COALESCE(fromdate, start_date) AND COALESCE(todate, start_date)
        GROUP BY
            month
        ) coeUT ON coeUT.max_date = cu.end_date
    WHERE
        eu.billable <= billableThreshold
        AND cu.id IN (
            SELECT
                coeut2.id
            FROM
                coe_utilization coeut2
            JOIN (
                SELECT
                    coeut1.end_date,
                    MAX(coeut1.created_date) AS lasted_date
                FROM
                    coe_utilization coeut1
                WHERE
                    coeut1.end_date = cu.end_date
                GROUP BY
                    coeut1.end_date
            ) coeLasted ON coeLasted.end_date = coeut2.end_date
                AND coeLasted.lasted_date = coeut2.created_date
        )
        AND e.id IN (
            SELECT
                es.employee_id
            FROM
                employee_status es
            WHERE
                es.status IN (1,2)
                AND es.employee_id = e.id
            GROUP BY
                es.employee_id
            HAVING
                MAX(es.status_date) = (
                    SELECT
                        MAX(es2.status_date)
                    FROM
                        employee_status es2
                    WHERE
                        es2.employee_id = es.employee_id
                )
        )
    GROUP BY
        e.id
    ) emp_free ON em.id = emp_free.id AND emp_free.daysFree > 0;
END;
$$ LANGUAGE plpgsql;
