package com.hitachi.coe.fullstack.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.hitachi.coe.fullstack.entity.CoeCoreTeam;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.hitachi.coe.fullstack.entity.Employee;

@DataJpaTest
@ActiveProfiles("data")
class EmployeeRepositoryTest {
    Employee employee, employeeOne;

    CoeCoreTeam team;

    @Autowired
    EmployeeRepository employeeRepository;

    @Mock
    public EmployeeRepository employeeRepositoryMock;

    @Autowired
    CoeCoreTeamRepository coeCoreTeamRepository;


    @BeforeEach
    void setUp() {
        team = new CoeCoreTeam();
        team.setCreated(new Date());
        team.setCreatedBy("admin");
        coeCoreTeamRepository.save(team);

        employee = new Employee();
        employee.setEmail("a.nguyen@hitachivantara.com");
        employee.setLdap("71269780");
        employee.setHccId("125351");
        employee.setName("Nguyen A 1");
        employee.setLegalEntityHireDate(new Timestamp(System.currentTimeMillis()));
        employee.setCreatedBy("admin");
        employee.setCreated(new Date());
        employee.setCoeCoreTeam(team);

        employeeRepository.save(employee);

    }

    @Test
    void testFindByEmailOrLdapOrHccId_whenValidEmailAndLdapAndHccId_thenReturnListEmployee() {

        List<Employee> employeeList = employeeRepository.findByEmailOrLdapOrHccId(employee.getEmail(),
                employee.getLdap(), employee.getHccId());
        assertNotNull(employeeList);
        assertEquals(employee.getLdap(), employeeList.get(0).getLdap());
        assertEquals(employee.getHccId(), employeeList.get(0).getHccId());
        assertEquals(employee.getEmail(), employeeList.get(0).getEmail());

    }

    @Test
    void testFindByEmailOrLdapOrHccId_whenValidLdap_thenReturnListEmployee() {

        List<Employee> employeeList = employeeRepository.findByEmailOrLdapOrHccId(null, employee.getLdap(), null);
        assertNotNull(employeeList);
        assertEquals(employee.getLdap(), employeeList.get(0).getLdap());
        assertEquals(employee.getHccId(), employeeList.get(0).getHccId());
        assertEquals(employee.getEmail(), employeeList.get(0).getEmail());

    }

    @Test
    void testFindByEmailOrLdapOrHccId_whenInvalidEmailAndLdapAndHccId_thenEmptyList() {

        List<Employee> employeeList = employeeRepository.findByEmailOrLdapOrHccId("123", "123", "123");
        assertNotNull(employeeList);
        assertEquals(0, employeeList.size());

    }

    @Test
    void testFindByHccId_whenValidHccId_thenReturnEmployee() {

        Employee employeeReturn = employeeRepository.findByHccId(employee.getHccId());
        assertNotNull(employee);
        assertEquals(employee.getLdap(), employeeReturn.getLdap());
        assertEquals(employee.getHccId(), employeeReturn.getHccId());
        assertEquals(employee.getEmail(), employeeReturn.getEmail());

    }

    @Test
    void testFindByHccId_whenInvalidHccId_thenNull() {

        Employee employeeReturn = employeeRepository.findByHccId("321");
        assertNull(employeeReturn);

    }

    @Test
    void testFindByHccIdAndLdap_whenValidLdapAndHccId_thenReturnEmployee() {

        Employee employeeReturn = employeeRepository.findByHccIdAndLdap(employee.getHccId(), employee.getLdap());
        assertNotNull(employee);
        assertEquals(employee.getLdap(), employeeReturn.getLdap());
        assertEquals(employee.getHccId(), employeeReturn.getHccId());
        assertEquals(employee.getEmail(), employeeReturn.getEmail());

    }

    @Test
    void testFindByHccIdAndLdap_whenInvalidLdap_thenNull() {

        Employee employeeReturn = employeeRepository.findByHccIdAndLdap(employee.getHccId(), "321");
        assertNull(employeeReturn);
    }

    @Test
    void testGetEmployeesByTeam_withValidTeam_returnEmployee() {
        employeeOne = new Employee();
        employeeOne.setEmail("a.nguyen@hitachivantara.com");
        employeeOne.setLdap("71269780");
        employeeOne.setHccId("125351");
        employeeOne.setName("Nguyen A 1");
        employeeOne.setLegalEntityHireDate(new Timestamp(System.currentTimeMillis()));
        employeeOne.setCreatedBy("admin");
        employeeOne.setCreated(new Date());
        employeeOne.setCoeCoreTeam(team);

        List<Employee> employeeExpect = new ArrayList<>();
        employeeExpect.add(employeeOne);

        List<Employee> employeeList = employeeRepository.findByCoeCoreTeamId(team.getId());

        assertNotNull(employeeList);
        assertEquals(employeeExpect.size(), employeeList.size());
        assertEquals(employeeExpect.get(0).getHccId(), employeeList.get(0).getHccId());
    }

    @Test
    void testGetEmployeesByTeam_withInValidTeam_returnEmptyResult() {
        List<Employee> employeeList = employeeRepository.findByCoeCoreTeamId(null);
        assertNotNull(employeeList);
        assertEquals(0, employeeList.size());
    }

    @Test
    void testFindByLdap_whenValidLdap_thenReturnOptionalEmployee(){
        Integer id = 4;
        String ldap = "125284";
        Employee employee = new Employee();
        employee.setId(4);
        employee.setLdap(ldap);
        Optional<Employee> employeeOptional = Optional.of(employee);
        when(employeeRepositoryMock.findByLdap(ldap)).thenReturn(employeeOptional);
        Optional<Employee> result = employeeRepositoryMock.findByLdap(ldap);
        assertNotNull(result);
        assertEquals(result.get().getId(), id);
        assertEquals(result.get().getLdap(), ldap);
    }

    @Test
    void testFindByLdap_whenInValidLdap_thenReturnNull(){
        String ldap = "1";
        when(employeeRepositoryMock.findByLdap(ldap)).thenReturn(Optional.empty());
        Optional<Employee> result = employeeRepositoryMock.findByLdap(ldap);
        assertFalse(result.isPresent());
    }
}
