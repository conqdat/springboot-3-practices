package com.hitachi.coe.fullstack.transformation;

import org.springframework.stereotype.Component;

import com.hitachi.coe.fullstack.entity.EmployeeRole;
import com.hitachi.coe.fullstack.model.EmployeeRoleModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class use to transform EmployeeRole entity to EmployeeRoleModel
 *
 * @author tminhto
 * @see EmployeeRole
 * @see EmployeeRoleModel
 */
@Component
public class EmployeeRoleTransformer extends AbstractCopyPropertiesTransformer<EmployeeRole, EmployeeRoleModel>
        implements EntityToModelTransformer<EmployeeRole, EmployeeRoleModel, Integer> {

    /**
     * Transformer array entities to array DTO.
     *
     * @param entities {@link List} of {@link EmployeeRole}
     * @return {@link List} of {@link EmployeeRoleModel}
     */
    public List<EmployeeRoleModel> applyList(List<EmployeeRole> entities) {
        if (null == entities || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream().map(this::apply)
                .collect(Collectors.toList());
    }
}
