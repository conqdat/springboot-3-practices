package com.hitachi.coe.fullstack.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectCountModel {
	Integer buId;
	String buName;
	TotalProject counts;
}
