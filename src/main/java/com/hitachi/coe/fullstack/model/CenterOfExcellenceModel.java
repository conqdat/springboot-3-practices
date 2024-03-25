package com.hitachi.coe.fullstack.model;

import java.util.List;

import com.hitachi.coe.fullstack.model.base.AuditModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CenterOfExcellenceModel extends AuditModel<Integer> {

	private static final long serialVersionUID = -4763725564504253497L;

	private String code;

	private String name;

	// don't do this in CoeCoreTeamModel it already have CenterOfExcellenceModel, it
	// will cause infinite loop in 2 transformer
	// private List<CoeCoreTeamModel> coeCoreTeamModelList;
}
