package com.hitachi.coe.fullstack.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TotalProject {
	private Long totalProject;
	private Long processProject;
	private Long doneProject;
}
