package com.hitachi.coe.fullstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hitachi.coe.fullstack.entity.User;

/**
 * The Interface UserRepository is using to access User table.
 *
 * @author ktrandangnguyen
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Find user by name.
     *
     * @param name The name of the user to find.
     * @return The user that matches the provided name.
     * @author tminhto
     */
    User findByName(@Param("name") String name);

    /**
     * Filters users that are in the same group base on group id.
     *
     * @param groupId The group id to filter by. If null, no filtering
     *                by group id will be applied.
     * @return A list of users that have the same group id.
     * @author ktrandangnguyen
     */
    List<User> findAllByGroupId(@Param("groupId") Integer groupId);
}
