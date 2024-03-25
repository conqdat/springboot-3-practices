package com.hitachi.coe.fullstack.transformation;

import com.hitachi.coe.fullstack.entity.Branch;
import com.hitachi.coe.fullstack.entity.Location;
import com.hitachi.coe.fullstack.model.BranchModel;
import com.hitachi.coe.fullstack.transformation.base.AbstractCopyPropertiesTransformer;
import com.hitachi.coe.fullstack.transformation.base.EntityToModelTransformer;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The BranchTransformer is convert entity to DTO;
 *
 * @author lphanhoangle
 */
@Slf4j
@Component
public class BranchTransformer extends AbstractCopyPropertiesTransformer<Branch, BranchModel>
		implements EntityToModelTransformer<Branch, BranchModel, Integer> {

	/**
	 * Transformer array entities to array DTO.
	 *
	 * @param entities {@link List} of {@link Branch}
	 * @return {@link List} of {@link BranchModel}
	 */
	public List<BranchModel> applyList(List<Branch> entities) {
		if (ObjectUtils.isEmpty(entities)) {
			return Collections.emptyList();
		}

		return entities.stream().map(this::apply).collect(Collectors.toList());
	}

	@Override
	public BranchModel apply(Branch branch) {
		if (branch == null) {
			log.debug("BranchTransformer::apply -> Branch is null");
			return null;
		}
		BranchModel model = new BranchModel();
		String branchName = branch.getName() != null
				? branch.getName()
				: "";
		String locationName = branch.getLocation() != null
				? branch.getLocation().getName()
				: "";
		String branchAndLocationName = branchName + " - " + locationName;
		model.setId(branch.getId());
		model.setName(branch.getName());
		model.setCode(branch.getCode());
		model.setLabel(branchAndLocationName);
		return model;
	}
}
