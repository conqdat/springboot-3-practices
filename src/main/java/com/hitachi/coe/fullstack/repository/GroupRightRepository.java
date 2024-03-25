package com.hitachi.coe.fullstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hitachi.coe.fullstack.entity.GroupRight;

@Repository
public interface GroupRightRepository extends JpaRepository<GroupRight, Integer> {
    /**
     * Find list of group right by group id
     *
     * @param groupId the group id to find the group right list
     * @return return a list of group right
     * @author tminhto
     */
    List<GroupRight> findAllByGroupId(@Param("groupId") Integer groupId);

}
