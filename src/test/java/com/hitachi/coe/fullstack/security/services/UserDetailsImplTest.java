package com.hitachi.coe.fullstack.security.services;

import com.hitachi.coe.fullstack.model.GroupRightModel;
import com.hitachi.coe.fullstack.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Test class for {@link UserDetailsImpl}.
 *
 * @author tminhto
 */
@Slf4j
class UserDetailsImplTest {

    @Test
    void testUserDetailsImpl_whenUserModelAndGroupRightModelsAreValid() {
        UserModel userModel = new UserModel();
        userModel.setName("testName");
        userModel.setPassword("testPassword");
        List<GroupRightModel> groupRightModels = List.of(
                new GroupRightModel() {{
                    setRightCode("GET");
                    setRightModule("BRANCH");
                }},
                new GroupRightModel() {{
                    setRightCode("POST");
                    setRightModule("EMPLOYEE");
                }}
        );
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(userModel, groupRightModels);
        Assertions.assertEquals(userModel.getName(), userDetailsImpl.getUserModel().getName());
        Assertions.assertEquals(userModel.getPassword(), userDetailsImpl.getPassword());
        Assertions.assertEquals(groupRightModels, userDetailsImpl.getGroupRightModels());
        Assertions.assertEquals(2, userDetailsImpl.getAuthorities().size());
        Assertions.assertEquals(userModel.getName(), userDetailsImpl.getUsername());
        Assertions.assertEquals("GET_BRANCH", userDetailsImpl.getAuthorities().toArray()[0].toString());
        Assertions.assertEquals("POST_EMPLOYEE", userDetailsImpl.getAuthorities().toArray()[1].toString());
    }

    @Test
    void testUserDetailsImpl_whenUserModelAndGroupRightModelsAreNull_thenThrowNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> new UserDetailsImpl(null, null));
    }

    @Test
    void testUserDetailsImpl_whenUserModelAndGroupRightModelsAreEmpty_thenThrowNullPointerException() {
        UserDetailsImpl userDetails = new UserDetailsImpl(new UserModel(), List.of());
        Assertions.assertEquals(0, userDetails.getAuthorities().size());
        Assertions.assertNull(userDetails.getPassword());
        Assertions.assertNull(userDetails.getUsername());
        Assertions.assertEquals(0, userDetails.getGroupRightModels().size());
        Assertions.assertNull(userDetails.getUserModel().getName());
        Assertions.assertNull(userDetails.getUserModel().getPassword());
    }
}
