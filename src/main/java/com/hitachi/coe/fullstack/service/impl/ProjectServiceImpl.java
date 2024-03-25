package com.hitachi.coe.fullstack.service.impl;

import com.hitachi.coe.fullstack.constant.CommonConstant;
import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.entity.BusinessDomain;
import com.hitachi.coe.fullstack.entity.BusinessUnit;
import com.hitachi.coe.fullstack.entity.Project;
import com.hitachi.coe.fullstack.entity.ProjectStatus;
import com.hitachi.coe.fullstack.entity.ProjectTech;
import com.hitachi.coe.fullstack.entity.ProjectType;
import com.hitachi.coe.fullstack.entity.SkillSet;
import com.hitachi.coe.fullstack.exceptions.CoEException;
import com.hitachi.coe.fullstack.exceptions.InvalidDataException;
import com.hitachi.coe.fullstack.model.BusinessDomainModel;
import com.hitachi.coe.fullstack.model.ExcelErrorDetail;
import com.hitachi.coe.fullstack.model.ExcelResponseModel;
import com.hitachi.coe.fullstack.model.IProjectModel;
import com.hitachi.coe.fullstack.model.IResultOfCountProject;
import com.hitachi.coe.fullstack.model.ImportResponse;
import com.hitachi.coe.fullstack.model.ProjectCountModel;
import com.hitachi.coe.fullstack.model.ProjectImportModel;
import com.hitachi.coe.fullstack.model.ProjectModel;
import com.hitachi.coe.fullstack.model.ProjectSearchModel;
import com.hitachi.coe.fullstack.model.ProjectUpdateModel;
import com.hitachi.coe.fullstack.model.SkillSetModel;
import com.hitachi.coe.fullstack.model.TotalProject;
import com.hitachi.coe.fullstack.model.common.ErrorLineModel;
import com.hitachi.coe.fullstack.model.common.ErrorModel;
import com.hitachi.coe.fullstack.repository.BusinessDomainRepository;
import com.hitachi.coe.fullstack.repository.BusinessUnitRepository;
import com.hitachi.coe.fullstack.repository.PracticeRepository;
import com.hitachi.coe.fullstack.repository.ProjectRepository;
import com.hitachi.coe.fullstack.repository.ProjectTypeRepository;
import com.hitachi.coe.fullstack.repository.SkillSetRepository;
import com.hitachi.coe.fullstack.service.EmployeeLevelService;
import com.hitachi.coe.fullstack.service.ProjectService;
import com.hitachi.coe.fullstack.service.ProjectTechService;
import com.hitachi.coe.fullstack.transformation.BusinessDomainTransformer;
import com.hitachi.coe.fullstack.transformation.ProjectModelTransformer;
import com.hitachi.coe.fullstack.transformation.ProjectSearchModelTransformer;
import com.hitachi.coe.fullstack.transformation.ProjectTransformer;
import com.hitachi.coe.fullstack.transformation.ProjectTypeTransformer;
import com.hitachi.coe.fullstack.transformation.SkillSetTransformer;
import com.hitachi.coe.fullstack.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = {CoEException.class, InvalidDataException.class, Exception.class})
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PracticeRepository practiceRepository;

    @Autowired
    private BusinessDomainRepository businessDomainRepository;
    @Autowired
    private ProjectTransformer projectTransformer;
    @Autowired
    private ProjectTypeRepository projectTypeRepository;
    @Autowired
    private ProjectModelTransformer projectModelTransformer;
    @Autowired
    private ProjectSearchModelTransformer projectSearchModelTransformer;
    @Autowired
    private ProjectTechService projectTechService;
    @Autowired
    private EmployeeLevelService employeeLevelService;

    @Autowired
    private SkillSetRepository skillSetRepository;

    @Autowired
    private SkillSetTransformer skillSetTransformer;

    @Autowired
    private BusinessDomainTransformer businessDomainTransformer;

    @Autowired
    private ProjectTypeTransformer projectTypeTransformer;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Override
    public ImportResponse importProject(ExcelResponseModel listOfProject) {
        ImportResponse importResponse = new ImportResponse();
        int totalSuccess = 0;
        HashMap<Integer, Object> dataList = listOfProject.getData();
        List<ExcelErrorDetail> errorList = listOfProject.getErrorDetails();
        List<ErrorModel> errorModelList = ErrorModel.importErrorDetails(errorList);
        List<BusinessDomain> businessDomainList = businessDomainRepository.findAll();
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        List<ProjectType> projectTypeList = projectTypeRepository.findAll();
        List<String> businessUnitsCode = businessUnitList.stream().map(BusinessUnit::getCode).collect(Collectors.toList());
        List<String> projectTypeListName = projectTypeList.stream().map(ProjectType::getName).collect(Collectors.toList());
        List<String> businessDomainsName = businessDomainList.stream().map(BusinessDomain::getName).collect(Collectors.toList());

        for (Map.Entry<Integer, Object> entry : dataList.entrySet()) {
            ProjectImportModel projectImport = (ProjectImportModel) entry.getValue();
            Project project = projectRepository.findByCode(projectImport.getProjectCode());
            ProjectStatus projectStatus = ProjectStatus.nameOf(projectImport.getProjectStatus().trim());
            String buCode = projectImport.getBusinessUnit().trim();
            String bdName = projectImport.getBusinessDomain().trim();
            String ctName = projectImport.getContractType().trim();
            if (ObjectUtils.isEmpty(project)
                    && businessUnitsCode.contains(buCode)
                    && businessDomainsName.contains(bdName)
                    && projectTypeListName.contains(ctName)
                    && projectStatus != null) {

                Project projectInsert = new Project();
                projectInsert.setCode(projectImport.getProjectCode());
                projectInsert.setCustomerName(projectImport.getCustomerName());
                projectInsert.setName(projectImport.getProjectName());
                projectInsert.setStatus(projectStatus);
                projectInsert.setStartDate(projectImport.getStartDate());
                projectInsert.setEndDate(projectImport.getEndDate());
                projectInsert.setDescription(projectImport.getDescription());
                projectInsert.setProjectManager(projectImport.getProjectManager());
                projectInsert.setCreatedBy(CommonConstant.CREATED_BY_ADMINISTRATOR);
                projectInsert.setCreated(new Date());
                projectInsert.setStatus(projectStatus);
                projectInsert.setProjectType(projectTypeList.get(projectTypeListName.indexOf(ctName)));
                projectInsert.setBusinessDomain(businessDomainList.get(businessDomainsName.indexOf(bdName)));
                projectInsert.setBusinessUnit(businessUnitList.get(businessUnitsCode.indexOf(buCode)));

                totalSuccess++;

                Project projectSave = projectRepository.save(projectInsert);
                projectTechService.addProjectListSkill(projectSave, projectImport.getSkillSetList().trim().split(","));

            } else {
                errorModelList.add(new ErrorModel(entry.getKey(), ErrorLineModel.importProjectErrorDetails(project, projectStatus == null ? projectImport.getProjectStatus().trim() : null,
                        businessUnitsCode, buCode, businessDomainsName, bdName, projectTypeListName, ctName)));
            }
        }

        ErrorModel.sortModelsByLine(errorModelList);
        importResponse.setTotalRows(listOfProject.getTotalRows());
        importResponse.setErrorRows(errorModelList.size());
        importResponse.setSuccessRows(totalSuccess);
        importResponse.setErrorList(errorModelList);
        return importResponse;
    }

    /*
     * (non-Javadoc) 
     * Edit 1: 2023-11-09 -> refactor code, organize validating code (tminhto)
     */
    @Override
    public Integer add(ProjectModel projectModel) {
        // validating
        if (projectModel == null) {
            throw new CoEException(
                    ErrorConstant.CODE_PROJECT_MODEL_NULL,
                    ErrorConstant.MESSAGE_PROJECT_MODEL_NULL);
        }
        Project existingProject = projectRepository.findByCode(projectModel.getCode());
        if (existingProject != null) {
            throw new CoEException(
                    ErrorConstant.CODE_PROJECT_CODE_EXIST,
                    ErrorConstant.MESSAGE_PROJECT_CODE_EXIST);
        }
        Optional<ProjectStatus> projectStatus = ProjectStatus.optValueOf(projectModel.getStatus());
        if (projectStatus.isEmpty()) {
            throw new CoEException(
                    ErrorConstant.CODE_PROJECT_STATUS_NOT_EXIST,
                    ErrorConstant.MESSAGE_STATUS_PROJECT_NOT_EXIST);
        }
        if (projectModel.getProjectType() == null) {
            throw new CoEException(
                    ErrorConstant.CODE_PROJECT_TYPE_NULL,
                    ErrorConstant.MESSAGE_PROJECT_TYPE_NULL);
        }
        if (projectModel.getBusinessDomain() == null) {
            throw new CoEException(
                    ErrorConstant.CODE_BUSINESS_DOMAIN_NULL,
                    ErrorConstant.MESSAGE_BUSINESS_DOMAIN_NULL);
        }
        if (projectModel.getBusinessUnitId() == null) {
            throw new CoEException(
                    ErrorConstant.CODE_BUSINESS_UNIT_NULL,
                    ErrorConstant.MESSAGE_BUSINESS_UNIT_NULL);
        }
        Optional<ProjectType> projectTypeOpt = projectTypeRepository.findById(projectModel.getProjectType().getId());
        if (projectTypeOpt.isEmpty()) {
            throw new CoEException(
                    ErrorConstant.CODE_PROJECT_TYPE_DO_NOT_EXIST,
                    ErrorConstant.MESSAGE_PROJECT_TYPE_DO_NOT_EXIST);
        }
        Optional<BusinessDomain> bussinessDomainOpt = businessDomainRepository
                .findById(projectModel.getBusinessDomain().getId());
        if (bussinessDomainOpt.isEmpty()) {
            throw new CoEException(
                    ErrorConstant.CODE_BUSINESS_DOMAIN_DO_NOT_EXIST,
                    ErrorConstant.MESSAGE_BUSINESS_DOMAIN_DO_NOT_EXIST);
        }
        Optional<BusinessUnit> businessUnitOpt = businessUnitRepository.findById(projectModel.getBusinessUnitId());
        if (businessUnitOpt.isEmpty()) {
            throw new CoEException(
                    ErrorConstant.CODE_BUSINESS_UNIT_DO_NOT_EXIST,
                    ErrorConstant.MESSAGE_BUSINESS_UNIT_DO_NOT_EXIST);
        }
        Project project = projectModelTransformer.apply(projectModel);
        if (project == null) {
            throw new CoEException(
                    ErrorConstant.CODE_PROJECT_NULL,
                    ErrorConstant.MESSAGE_PROJECT_NULL);
        }
        // save project and list of skillset to project_tech table
        Project savedProject = projectRepository.save(project);
        projectModel.getSkillSets().forEach(skill -> projectTechService.addProjectSkill(savedProject, skill));
        return savedProject.getId();
    }

    @Override
    public Page<ProjectSearchModel> searchProjects(String keyword, String bdName, String ptName, String projectManager, Integer status,
                                                   String fromDateStr, String toDateStr, Integer no, Integer limit, String sortBy, Boolean desc) {

        // sort by field of project
        Sort sort = Sort.by(sortBy);
        if (desc != null) {
            sort = sort.descending();
        }
        Timestamp fromDate = CommonUtils.convertStringToTimestamp(fromDateStr);
        Timestamp toDate = CommonUtils.convertStringToTimestamp(toDateStr);

        ProjectStatus statusSearch = null;
        if (status != null) {
            for (ProjectStatus statusList : ProjectStatus.values()) {
                if (status == statusList.getValue()) {
                    statusSearch = ProjectStatus.valueOf(status);
                    break;
                }
            }
            if (statusSearch == null) {
                statusSearch = ProjectStatus.ERROR;
            }
        }
        // return page of list projects
        return projectRepository.filterProjects(keyword, bdName, ptName, projectManager, statusSearch, fromDate, toDate,
                PageRequest.of(no, limit, sort)).map(p -> projectSearchModelTransformer.apply(p));
        // convert from projectEntity to projectModel
    }

    @Override
    public ProjectModel update(final ProjectUpdateModel projectUpdateModel) {
        final Project findProject = projectRepository.findByCode(projectUpdateModel.getCode());
        final Timestamp startDate = CommonUtils.convertStringToTimestamp(projectUpdateModel.getStartDate());
        final Timestamp endDate = CommonUtils.convertStringToTimestamp(projectUpdateModel.getEndDate());

        Optional<ProjectStatus> projectStatus = ProjectStatus.optValueOf(projectUpdateModel.getStatus());
        if (projectStatus.isEmpty()) {
            throw new CoEException(
                    ErrorConstant.CODE_PROJECT_STATUS_NOT_EXIST,
                    ErrorConstant.MESSAGE_STATUS_PROJECT_NOT_EXIST);
        }

        final Optional<Project> project = projectRepository.findById(projectUpdateModel.getProjectId());
        if (project.isEmpty()) {
            throw new CoEException(
                    ErrorConstant.CODE_PROJECT_NOT_FOUND,
                    ErrorConstant.MESSAGE_PROJECT_NOT_FOUND);
        }

        final Project existingProject = project.get();
        final List<SkillSetModel> skillSetModelList = new ArrayList<>();

        if (Objects.requireNonNull(startDate).after(endDate)) {
            throw new CoEException(
                    ErrorConstant.CODE_INVALID_START_DATE_END_DATE,
                    ErrorConstant.MESSAGE_INVALID_START_DATE_END_DATE);
        }
        if (projectUpdateModel.getProjectTypeId() == null) {
            throw new CoEException(
                    ErrorConstant.CODE_PROJECT_TYPE_NULL,
                    ErrorConstant.MESSAGE_PROJECT_TYPE_NULL);
        }
        final Optional<ProjectType> projectType = projectTypeRepository.findById(projectUpdateModel.getProjectTypeId());
        if (projectType.isEmpty()) {
            throw new CoEException(
                    ErrorConstant.CODE_PROJECT_TYPE_DO_NOT_EXIST,
                    ErrorConstant.MESSAGE_PROJECT_TYPE_DO_NOT_EXIST);
        }
        if (projectUpdateModel.getBusinessDomainId() == null) {
            throw new CoEException(
                    ErrorConstant.CODE_BUSINESS_DOMAIN_NULL,
                    ErrorConstant.MESSAGE_BUSINESS_DOMAIN_NULL);
        }
        final Optional<BusinessDomain> businessDomain = businessDomainRepository
                .findById(projectUpdateModel.getBusinessDomainId());
        if (businessDomain.isEmpty()) {
            throw new CoEException(
                    ErrorConstant.CODE_BUSINESS_DOMAIN_DO_NOT_EXIST,
                    ErrorConstant.MESSAGE_BUSINESS_DOMAIN_DO_NOT_EXIST);
        }

        if (projectUpdateModel.getBusinessUnitId() == null) {
            throw new CoEException(
                    ErrorConstant.CODE_BUSINESS_UNIT_NULL,
                    ErrorConstant.MESSAGE_BUSINESS_UNIT_NULL);
        }
        final Optional<BusinessUnit> businessUnit = businessUnitRepository
                .findById(projectUpdateModel.getBusinessUnitId());
        if (businessUnit.isEmpty())
            throw new CoEException(
                    ErrorConstant.CODE_BUSINESS_UNIT_DO_NOT_EXIST,
                    ErrorConstant.MESSAGE_BUSINESS_UNIT_DO_NOT_EXIST);

        if (ObjectUtils.isEmpty(findProject)) {
            existingProject.setCode(projectUpdateModel.getCode());
        }

        projectTechService.deleteProjectTechByProject(existingProject.getId());
        existingProject.setName(projectUpdateModel.getName());
        existingProject.setDescription(projectUpdateModel.getDescription());
        existingProject.setCustomerName(projectUpdateModel.getCustomerName());
        existingProject.setProjectManager(projectUpdateModel.getProjectManager());
        existingProject.setStatus(projectStatus.get());
        existingProject.setStartDate(startDate);
        existingProject.setEndDate(endDate);
        existingProject.setBusinessDomain(businessDomain.get());
        existingProject.setProjectType(projectType.get());
        existingProject.setBusinessUnit(businessUnit.get());
        projectRepository.save(existingProject);
        projectUpdateModel.getProjectsTech().forEach(projectTechId -> {
            final Optional<SkillSet> skillSet = skillSetRepository.findById(projectTechId);
            if (skillSet.isEmpty()) {
                throw new CoEException(
                        ErrorConstant.CODE_SKILL_SET_DO_NOT_EXIST,
                        ErrorConstant.MESSAGE_SKILL_SET_DO_NOT_EXIST);
            }
            final ProjectTech projectTech = new ProjectTech();
            projectTech.setProject(existingProject);
            projectTech.setSkillSet(skillSet.get());
            skillSetModelList.add(skillSetTransformer.apply(skillSet.get()));
            projectTechService.saveProjectTech(projectTech);
        });
        ProjectModel saveProjectModel = projectTransformer.apply(existingProject);
        saveProjectModel.setSkillSets(skillSetModelList);
        return saveProjectModel;
    }


    @Override
    public ProjectModel closeProject(Integer projectId) {
        final Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new CoEException(ErrorConstant.CODE_PROJECT_NOT_FOUND,
                        ErrorConstant.MESSAGE_PROJECT_NOT_FOUND));
        if (existingProject.getStatus().equals(ProjectStatus.CLOSE)) {
            throw new CoEException(ErrorConstant.CODE_PROJECT_ALREADY_CLOSE,
                    ErrorConstant.MESSAGE_PROJECT_ALREADY_CLOSE);
        }
        final List<IProjectModel> iProjectModels = projectRepository.getProjectDetailById(projectId);
        if (ObjectUtils.isEmpty(iProjectModels)) {
            throw new CoEException(ErrorConstant.CODE_PROJECT_NOT_FOUND, ErrorConstant.MESSAGE_PROJECT_NOT_FOUND);
        }
        final List<SkillSetModel> skillSetModels = returnSkillSetModelList(iProjectModels);
        existingProject.setStatus(ProjectStatus.CLOSE);
        final Project savedProject = projectRepository.save(existingProject);
        final ProjectModel projectModel = projectTransformer.apply(savedProject);
        projectModel.setSkillSets(skillSetModels);
        return projectModel;
    }

    @Override
    public ProjectModel getProjectDetailById(Integer id) {
        final List<IProjectModel> iProjectModels = projectRepository.getProjectDetailById(id);

        if (ObjectUtils.isEmpty(iProjectModels))
            throw new CoEException(ErrorConstant.CODE_PROJECT_NOT_FOUND, ErrorConstant.MESSAGE_PROJECT_NOT_FOUND);

        final ProjectModel projectModel = new ProjectModel();
        final Optional<BusinessDomain> businessDomain;
        final Optional<ProjectType> projectType = projectTypeRepository.findById(iProjectModels.get(0).getProjectTypeId());

        if (!ObjectUtils.isEmpty(iProjectModels.get(0).getBusinessDomainId())) {
            businessDomain = businessDomainRepository.findById(iProjectModels.get(0).getBusinessDomainId());
            if (businessDomain.isEmpty()) {
                throw new CoEException(ErrorConstant.CODE_BUSINESS_DOMAIN_DO_NOT_EXIST, ErrorConstant.MESSAGE_BUSINESS_DOMAIN_DO_NOT_EXIST);
            }
            projectModel.setBusinessDomain(businessDomainTransformer.apply(businessDomain.get()));
        } else {
            projectModel.setBusinessDomain(new BusinessDomainModel());
        }


        if (projectType.isEmpty()) {
            throw new CoEException(ErrorConstant.CODE_PROJECT_TYPE_DO_NOT_EXIST, ErrorConstant.MESSAGE_PROJECT_TYPE_DO_NOT_EXIST);
        }

        final List<SkillSetModel> skillSetModels = returnSkillSetModelList(iProjectModels);
        projectModel.setProjectType(projectTypeTransformer.apply(projectType.get()));
        projectModel.setId(iProjectModels.get(0).getProjectId());
        projectModel.setCode(iProjectModels.get(0).getCode());
        projectModel.setName(iProjectModels.get(0).getProjectName());
        projectModel.setProjectManager(iProjectModels.get(0).getProjectManager());
        projectModel.setDescription(iProjectModels.get(0).getDescription());
        projectModel.setStartDate(iProjectModels.get(0).getStartDate());
        projectModel.setEndDate(iProjectModels.get(0).getEndDate());
        projectModel.setStatus(iProjectModels.get(0).getStatus());
        projectModel.setCustomerName(iProjectModels.get(0).getCustomerName());
        projectModel.setBusinessUnitId(iProjectModels.get(0).getBusinessUnitId());
        projectModel.setSkillSets(skillSetModels);
        return projectModel;
    }

    /**
     * This method use to return list of SkillSetModel
     * 
     * @param iProjectModels list of IProjectModel to loop to find skillSetId list
     * @return list of SkillSetModel
     * @author tminhto
     */
    private List<SkillSetModel> returnSkillSetModelList(List<IProjectModel> iProjectModels) {
        final List<SkillSetModel> skillSetModels = new ArrayList<>();
        if (!ObjectUtils.isEmpty(iProjectModels.get(0).getSkillSetId())) {
            for (IProjectModel iProjectModel : iProjectModels) {
                final Optional<SkillSet> skillSet = skillSetRepository.findById(iProjectModel.getSkillSetId());
                if (skillSet.isEmpty()) {
                    throw new CoEException(ErrorConstant.CODE_SKILL_SET_DO_NOT_EXIST, ErrorConstant.MESSAGE_SKILL_SET_DO_NOT_EXIST);
                }
                skillSetModels.add(skillSetTransformer.apply(skillSet.get()));
            }
        }
        return skillSetModels;
    }

    @Override
    public ProjectModel updateProjectStatus(final Integer projectId, final Integer status) {
        final Optional<Project> projectOpt = projectRepository.findById(projectId);
        final List<IProjectModel> iProjectModels = projectRepository.getProjectDetailById(projectId);
        if (projectOpt.isEmpty()) {
            throw new CoEException(ErrorConstant.CODE_PROJECT_NOT_FOUND, ErrorConstant.MESSAGE_PROJECT_NOT_FOUND);
        }
        if (ObjectUtils.isEmpty(iProjectModels)) {
            throw new CoEException(ErrorConstant.CODE_PROJECT_NOT_FOUND, ErrorConstant.MESSAGE_PROJECT_NOT_FOUND);
        }
        final ProjectStatus projectStatus = ProjectStatus.valueOf(status);
        final List<SkillSetModel> skillSetModels = returnSkillSetModelList(iProjectModels);
        Project existingProject = projectOpt.get();
        existingProject.setStatus(projectStatus);
        Project savedProject = projectRepository.save(existingProject);
        ProjectModel projectModel = projectTransformer.apply(savedProject);
        projectModel.setSkillSets(skillSetModels);
        return projectModel;
    }

    @Override
    public List<ProjectModel> getNewlyCreatedProject(Integer buId) {
        return projectTransformer.applyList(projectRepository.getNewlyCreatedProject(buId));
    }

    @Override
    public Integer countNewlyCreatedProject(Integer buId) {
        return projectRepository.countNewlyCreatedProject(buId);
    }

    @Override
    public List<ProjectModel> getExpiredProject(Integer buId) {
        return projectTransformer.applyList(projectRepository.getExpiredProject(buId));
    }

    @Override
    public Integer countExpiredProject(Integer buId) {
        return projectRepository.countExpiredProject(buId);
    }

    @Override
    public Integer countProjectByBuIdAndStatus(Integer buId, Integer status) {
        Optional<ProjectStatus> projectStatus = ProjectStatus.optValueOf(status);
        if (projectStatus.isEmpty()) {
            throw new CoEException(ErrorConstant.CODE_PROJECT_STATUS_NOT_EXIST, ErrorConstant.MESSAGE_STATUS_PROJECT_NOT_EXIST);
        }
        return projectRepository.countProjectByBuIdAndStatus(buId, projectStatus.get().getValue());
    }

    @Override
    public List<ProjectCountModel> countProjectsForPieChart(Integer buId) {
        List<IResultOfCountProject> results = projectRepository.countProjectsForPieChart(buId);

        List<ProjectCountModel> countProject = new ArrayList<>();

        for (IResultOfCountProject result : results) {
            Long total = result.getTotalProject();
            Long totalNewlyCreated = result.getTotalNewProject();
            Long totalExpired = result.getTotalCloseProject();

            TotalProject totalProject = new TotalProject(total, totalNewlyCreated, totalExpired);

            ProjectCountModel finalModel = new ProjectCountModel(result.getBuId(), result.getBuName(), totalProject);
            countProject.add(finalModel);
        }
        return countProject;
    }
}