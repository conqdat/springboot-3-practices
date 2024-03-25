SELECT setval('"employee_id_seq"', (SELECT MAX(id) FROM public.employee)); 

SELECT setval('"coe_core_team_id_seq"', (SELECT MAX(id) FROM public.coe_core_team)); 

SELECT setval('"employee_status_id_seq"', (SELECT MAX(id) FROM public.employee_status)); 

INSERT INTO public.employee(
	ldap, hcc_id, name, email, legal_entity_hire_date, business_unit_id, branch_id, coe_core_team_id, created_by, created_date, updated_by, updated_date)
	VALUES ('999999', '999999', 'Administrator', 'administrator@hitachivantara.com','2010-01-01', 1, 1, 1, 'admin', now(), null, null);

INSERT INTO public.coe_core_team(
    code, name, coe_id, sub_leader_id, status, created_by, created_date, updated_by, updated_date)
    VALUES ('DEF', 'Default team', 8, (SELECT employee.id FROM public.employee WHERE employee.hcc_id = '999999'), 0, 'admin', now(), null, null);

UPDATE public.employee 
    SET coe_core_team_id = (SELECT coe_core_team.id FROM public.coe_core_team WHERE coe_core_team.code = 'DEF')
    WHERE employee.hcc_id = '999999';

INSERT INTO public.employee_status(
	status_date, employee_id, status, description)
	VALUES (now(), (SELECT employee.id FROM public.employee WHERE employee.hcc_id = '999999'), 0, null);