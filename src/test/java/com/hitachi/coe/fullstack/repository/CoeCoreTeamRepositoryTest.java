package com.hitachi.coe.fullstack.repository;

import com.hitachi.coe.fullstack.entity.CenterOfExcellence;
import com.hitachi.coe.fullstack.entity.CoeCoreTeam;
import com.hitachi.coe.fullstack.entity.Employee;
import com.hitachi.coe.fullstack.entity.SurveyData;
import com.hitachi.coe.fullstack.model.CenterOfExcellenceModel;
import com.hitachi.coe.fullstack.model.CoeCoreTeamModel;
import com.hitachi.coe.fullstack.model.EmployeeModel;
import com.hitachi.coe.fullstack.model.IEmployeeUtilizationModel;
import com.hitachi.coe.fullstack.service.CoeCoreTeamService;
import org.h2.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("data")
class CoeCoreTeamRepositoryTest {
    CoeCoreTeamRepository coeCoreTeamRepository;

    @Autowired
    private CenterOfExcellenceRepository centerOfExcellenceRepository;

    CoeCoreTeamService coeCoreTeamService;
    CoeCoreTeam team, teamOne;
    CenterOfExcellence coe;
    Employee employee;
    @Mock
    private Pageable pageableMock;

    @Mock
    private Utils utils;

    @BeforeEach
    public void setUp(){
        team = new CoeCoreTeam();

        coe = new CenterOfExcellence();
        coe.setCode("STA");
        coe.setName("Solution/Technical Architecture");
        coe.setCreatedBy("admin");

        team.setCode("BE");
        team.setName("Back-end");
        team.setStatus(1);
        team.setSubLeaderId(1);
        team.setCenterOfExcellence(coe);
        team.setCreatedBy("admin");

        coeCoreTeamRepository.save(team);
    }
    @Test
    void testGetCoeTeamByCoeId() {
        CenterOfExcellence coe = new CenterOfExcellence();

        coe.setCreated(new Date());
        coe.setCreatedBy("admin");
        centerOfExcellenceRepository.save(coe);
        CoeCoreTeam coeCoreTeamExpected = new CoeCoreTeam();
        coeCoreTeamExpected.setCode("1");
        coeCoreTeamExpected.setName("Angular");
        coeCoreTeamExpected.setStatus(1);
        coeCoreTeamExpected.setSubLeaderId(1);
        coeCoreTeamExpected.setCenterOfExcellence(coe);
        coeCoreTeamExpected.setEmployees(Collections.emptyList());
        coeCoreTeamExpected.setCreated(new Date());
        coeCoreTeamExpected.setCreatedBy("admin");
        List<CoeCoreTeam> coeCoreTeamsExpected = new ArrayList<CoeCoreTeam>();
        coeCoreTeamsExpected.add(coeCoreTeamExpected);

        CoeCoreTeam coeCoreTeam = new CoeCoreTeam();
        coeCoreTeam.setCode("1");
        coeCoreTeam.setName("Angular");
        coeCoreTeam.setStatus(1);
        coeCoreTeam.setSubLeaderId(1);
        coeCoreTeam.setCenterOfExcellence(coe);
        coeCoreTeam.setEmployees(Collections.emptyList());
        coeCoreTeam.setCreated(new Date());
        coeCoreTeam.setCreatedBy("admin");
        coeCoreTeamRepository.save(coeCoreTeam);

        coe = centerOfExcellenceRepository.findById(coe.getId()).orElse(null);
        assertNotNull(coe, "CenterOfExcellence with ID 1 should exist. Got null.");
        List<CoeCoreTeam> coeTeamList = coeCoreTeamRepository.getAllByCenterOfExcellenceIdAndStatus(coe.getId(), 1);
        assertNotNull(coeTeamList);
        assertEquals(coeCoreTeamsExpected.size(), coeTeamList.size());
        assertEquals(coeCoreTeamsExpected.get(0).getName(), coeTeamList.get(0).getName());
    }

}

