CREATE OR REPLACE FUNCTION getQuantityOfEmployeesForEachSkill(
  branchId INTEGER,
  coeId INTEGER,
  skillIds character varying
)
RETURNS TABLE (buName VARCHAR(250), skillName VARCHAR(250), total BIGINT, level_1 BIGINT, level_2 BIGINT, level_3 BIGINT,
			   level_4 BIGINT, level_5 BIGINT)
AS $$
BEGIN
    RETURN QUERY
    SELECT 
	bu.name AS buName,
	ss.name AS skillName,
	COUNT(esk.employee_id) AS total,
	COUNT(CASE WHEN esk.skill_level = 1 THEN esk.employee_id ELSE NULL END) AS level_1,
  COUNT(CASE WHEN esk.skill_level = 2 THEN esk.employee_id ELSE NULL END) AS level_2,
  COUNT(CASE WHEN esk.skill_level = 3 THEN esk.employee_id ELSE NULL END) AS level_3,
  COUNT(CASE WHEN esk.skill_level = 4 THEN esk.employee_id ELSE NULL END) AS level_4,
  COUNT(CASE WHEN esk.skill_level = 5 THEN esk.employee_id ELSE NULL END) AS level_5
FROM public.employee e
JOIN public.business_unit bu ON e.business_unit_id = bu.id
JOIN public.employee_skill esk ON e.id = esk.employee_id
JOIN public.skill_set ss ON ss.id = esk.skill_set_id
JOIN coe_core_team team ON e.coe_core_team_id = team.id
JOIN center_of_excellence coe ON team.coe_id = coe.id
WHERE (e.branch_id = branchId OR branchId IS NULL)
	AND(coe.id = coeId OR coeId IS NULL)
	AND(esk.skill_set_id::TEXT IN (SELECT unnest(string_to_array(skillIds, ','))) OR skillIds IS NULL)
GROUP BY ss.name, bu.name
ORDER BY bu.name ASC;
END;
$$ LANGUAGE plpgsql;