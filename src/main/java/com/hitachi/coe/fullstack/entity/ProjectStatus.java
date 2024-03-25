package com.hitachi.coe.fullstack.entity;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.exceptions.CoEException;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public enum ProjectStatus {

    ON_HOLD(0),

    POC(1),

    OPEN(2),

    CLOSE(3),

    REOPEN(4),

    ONGOING(5),

    CANCEL(6),

    ERROR(-1);

    private final int value;

    ProjectStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Get enum by integer value
     *
     * @param integer integer to get enum for
     * @return an {@link ProjectStatus} enum, null if not found or null input
     */
    public static ProjectStatus valueOf(Integer integer) {
        if (Objects.isNull(integer)) {
            log.debug(ErrorConstant.MESSAGE_PROJECT_STATUS_REQUIRED);
            return null;
        }
        for (ProjectStatus type : ProjectStatus.values()) {
            if (type.getValue() == integer) {
                return type;
            }
        }
        return null;
    }

    /**
     * Get enum optional by integer value
     * 
     * @param value integer to get enum for
     * @return an {@link ProjectStatus} wrapped by {@link Optional}, Optional.empty
     *         if not found or null input
     */
    public static Optional<ProjectStatus> optValueOf(Integer value) {
        if (Objects.isNull(value)) {
            log.debug(ErrorConstant.MESSAGE_PROJECT_STATUS_REQUIRED);
            return Optional.empty();
        }
        for (ProjectStatus type : ProjectStatus.values()) {
            if (type.getValue() == value) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    /**
     * Get Project Status based on the provided conditions.
     *
     * @param str stand for string name of project status
     * @return the project status that match the string name
     * @author tquangpham
     */
    public static ProjectStatus nameOf(String str) {
        ProjectStatus projectStatus = null;
        switch (str.toLowerCase()) {
            case "closed":
                projectStatus = ProjectStatus.CLOSE;
                break;
            case "on-going":
                projectStatus = ProjectStatus.ONGOING;
                break;
            case "on-hold":
                projectStatus = ProjectStatus.ON_HOLD;
                break;
            case "poc":
                projectStatus = ProjectStatus.POC;
                break;
            case "re-open":
                projectStatus = ProjectStatus.REOPEN;
                break;
            case "open":
                projectStatus = ProjectStatus.OPEN;
                break;
            case "cancel":
                projectStatus = ProjectStatus.CANCEL;
                break;
            default:
                break;
        }
        return projectStatus;
    }
}

