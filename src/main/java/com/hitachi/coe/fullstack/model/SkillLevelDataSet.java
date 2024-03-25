package com.hitachi.coe.fullstack.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SkillLevelDataSet {
    List<Long> levelCnt;
    Long total;
}
