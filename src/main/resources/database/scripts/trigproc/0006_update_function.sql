
CREATE OR REPLACE FUNCTION public.piechartlevel(
	p_branch_id integer,
	p_core_team_id integer,
	p_coe_id integer)
    RETURNS TABLE(name character varying, total_employees bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
   BEGIN
    RETURN QUERY
    SELECT lv.code, COUNT(DISTINCT emp.id) AS total_employees
    FROM (
    SELECT DISTINCT ON (employee_id) employee_id, level_id
    FROM employee_level
    ORDER BY employee_id, level_date DESC
    ) emplv
    INNER JOIN level lv ON emplv.level_id = lv.id
    INNER JOIN employee emp ON emplv.employee_id = emp.id
	INNER JOIN (
		SELECT es.employee_id, MAX(es.status_date) AS max_status_date
		FROM employee_status es
		WHERE es.status <> 0
		GROUP BY es.employee_id
	) latest_status ON latest_status.employee_id = emp.id
	LEFT JOIN coe_core_team coeT ON emp.coe_core_team_id = coeT.id
    LEFT JOIN center_of_excellence coe ON coeT.coe_id = coe.id

    WHERE (emp.branch_id = COALESCE(p_branch_id, emp.branch_id) OR COALESCE(p_branch_id, null) = null)
    AND (emp.coe_core_team_id = COALESCE(p_core_team_id, emp.coe_core_team_id) OR COALESCE(p_core_team_id, null) = null)
    AND (coeT.coe_id = COALESCE(p_coe_id, coeT.coe_id) OR COALESCE(p_coe_id, null) = null)
    GROUP BY lv.code
    ORDER BY total_employees DESC;
   END;
$BODY$;