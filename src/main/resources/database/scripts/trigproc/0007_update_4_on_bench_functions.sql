--FUNCTION: getQuantityOfEmployeesOnBenchByBusinessUnit(integer)
--Created by ThuyTrinhThanhLe
CREATE OR REPLACE FUNCTION getQuantityOfEmployeesOnBenchByBusinessUnit(
  date DATE
)
RETURNS TABLE (businessUnitName VARCHAR(250), total BIGINT)
AS $$
BEGIN
RETURN QUERY
SELECT DISTINCT bu.name AS businessUnitName, coalesce(COUNT(on_bench.employee_id), 0) AS total
FROM employee e
    LEFT JOIN business_unit bu ON e.business_unit_id = bu.id
    LEFT JOIN (SELECT DISTINCT eobd.employee_id
          FROM employee_on_bench eob
              JOIN employee_on_bench_detail eobd ON eob.id = eobd.employee_on_bench_id
          WHERE eob.id = (SELECT eob1.id
                          FROM employee_on_bench eob1
                          WHERE EXTRACT(YEAR FROM date) = EXTRACT(YEAR FROM eob1.start_date)
                            AND EXTRACT(MONTH FROM date) = EXTRACT(MONTH FROM eob1.start_date)
                          ORDER BY eob1.created_date DESC
          LIMIT 1)
GROUP BY eobd.employee_id) on_bench ON on_bench.employee_id = e.id
GROUP BY businessUnitName
ORDER BY total DESC;
END;
$$ LANGUAGE plpgsql;


-- FUNCTION: getQuantityOfEmployeesOnBenchByLocation(integer)
-- Created by ThuyTrinhThanhLe
CREATE OR REPLACE FUNCTION getQuantityOfEmployeesOnBenchByLocation(
  date DATE
)
RETURNS TABLE (branchName VARCHAR(250), total BIGINT)
AS $$
BEGIN
RETURN QUERY
SELECT DISTINCT br.name AS branchName, coalesce(COUNT(on_bench.employee_id), 0) AS total
FROM employee e
    LEFT JOIN branch br ON e.branch_id = br.id
    LEFT JOIN (SELECT DISTINCT eobd.employee_id
          FROM employee_on_bench eob
              JOIN employee_on_bench_detail eobd ON eob.id = eobd.employee_on_bench_id
          WHERE eob.id = (SELECT eob1.id
                          FROM employee_on_bench eob1
                          WHERE EXTRACT(YEAR FROM date) = EXTRACT(YEAR FROM eob1.start_date)
                            AND EXTRACT(MONTH FROM date) = EXTRACT(MONTH FROM eob1.start_date)
                          ORDER BY eob1.created_date DESC
          LIMIT 1)
GROUP BY eobd.employee_id) on_bench ON on_bench.employee_id = e.id
GROUP BY branchName
ORDER BY total DESC;
END;
$$ LANGUAGE plpgsql;


-- FUNCTION: getQuantityOfEmployeesOnBenchByLevel(integer)
-- Created by ThuyTrinhThanhLe
CREATE OR REPLACE FUNCTION getQuantityOfEmployeesOnBenchByLevel(
  date DATE
)
RETURNS TABLE (levelName VARCHAR(250), total BIGINT)
AS $$
BEGIN
RETURN QUERY
SELECT DISTINCT el_l.level AS levelName, coalesce(COUNT(on_bench.employee_id), 0) AS total
FROM employee e
    LEFT JOIN (SELECT el.employee_id, l.name AS level
          FROM employee_level el
              JOIN level l ON el.employee_id = l.id) el_l ON el_l.employee_id = e.id
    LEFT JOIN (SELECT DISTINCT eobd.employee_id
          FROM employee_on_bench eob
              JOIN employee_on_bench_detail eobd ON eob.id = eobd.employee_on_bench_id
          WHERE eob.id = (SELECT eob1.id
                          FROM employee_on_bench eob1
                          WHERE EXTRACT(YEAR FROM date) = EXTRACT(YEAR FROM eob1.start_date)
                            AND EXTRACT(MONTH FROM date) = EXTRACT(MONTH FROM eob1.start_date)
                          ORDER BY eob1.created_date DESC
          LIMIT 1)
GROUP BY eobd.employee_id) on_bench ON on_bench.employee_id = e.id
GROUP BY levelName
ORDER BY total DESC;
END;
$$ LANGUAGE plpgsql;


-- FUNCTION: getQuantityOfEmployeesOnBenchByCoe(integer)
-- Created by ThuyTrinhThanhLe
CREATE OR REPLACE FUNCTION getQuantityOfEmployeesOnBenchByCoe(
  date DATE)
RETURNS TABLE (coeName VARCHAR(250), total BIGINT)
AS $$
BEGIN
RETURN QUERY
SELECT DISTINCT group_coe.coe AS coeName, coalesce(COUNT(on_bench.employee_id), 0) AS total
FROM employee e
    LEFT JOIN (SELECT t.id, coe.name AS coe
          FROM coe_core_team t
              JOIN center_of_excellence coe ON t.coe_id = coe.id) group_coe
        ON group_coe.id = e.coe_core_team_id
    LEFT JOIN (SELECT DISTINCT eobd.employee_id
          FROM employee_on_bench eob
              JOIN employee_on_bench_detail eobd ON eob.id = eobd.employee_on_bench_id
          WHERE eob.id = (SELECT eob1.id
                          FROM employee_on_bench eob1
                          WHERE EXTRACT(YEAR FROM date) = EXTRACT(YEAR FROM eob1.start_date)
                            AND EXTRACT(MONTH FROM date) = EXTRACT(MONTH FROM eob1.start_date)
                          ORDER BY eob1.created_date DESC
          LIMIT 1)
GROUP BY eobd.employee_id) on_bench ON on_bench.employee_id = e.id
GROUP BY coeName
ORDER BY total DESC;
END;
$$ LANGUAGE plpgsql;