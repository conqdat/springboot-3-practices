package com.hitachi.coe.fullstack.security.services;

import com.hitachi.coe.fullstack.entity.Group;
import com.hitachi.coe.fullstack.entity.GroupRight;
import com.hitachi.coe.fullstack.entity.User;
import com.hitachi.coe.fullstack.model.GroupRightModel;
import com.hitachi.coe.fullstack.model.UserModel;
import com.hitachi.coe.fullstack.repository.GroupRightRepository;
import com.hitachi.coe.fullstack.repository.UserRepository;
import com.hitachi.coe.fullstack.transformation.GroupRightTransformer;
import com.hitachi.coe.fullstack.transformation.UserTransformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserTransformer userTransformer;
    @Mock
    GroupRightRepository groupRightRepository;

    @Mock
    GroupRightTransformer groupRightTransformer;

    @Test
    void testLoadUserByUsername_whenUsernameIsValid_thenReturnUserDetails() {
        // prepare
        String username = "testUsername";
        User user = new User();
        user.setName(username);
        Group group = new Group();
        group.setId(1);
        user.setGroup(group);
        List<GroupRight> groupRights = List.of(new GroupRight());
        UserModel userModel = new UserModel();
        userModel.setName(username);
        userModel.setPassword("testPassword");
        GroupRight groupRight = new GroupRight();
        groupRight.setGroupId(1);
        GroupRightModel groupRightModel = new GroupRightModel();
        groupRightModel.setGroupId(1);
        groupRightModel.setRightCode("GET");
        groupRightModel.setRightModule("BRANCH");
        // when-then
        Mockito.when(userRepository.findByName(username)).thenReturn(user);
        Mockito.when(groupRightRepository.findAllByGroupId(group.getId())).thenReturn(groupRights);
        Mockito.when(userTransformer.apply(user)).thenReturn(userModel);
        Mockito.when(groupRightTransformer.apply(groupRight)).thenReturn(groupRightModel);
        // invoke
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // assert
        Assertions.assertEquals(userModel.getName(), userDetails.getUsername());
        Assertions.assertEquals(userModel.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(1, userDetails.getAuthorities().size());
        Assertions.assertEquals("GET_BRANCH", userDetails.getAuthorities().iterator().next().getAuthority());
        // verify
        Mockito.verify(userRepository).findByName(username);
        Mockito.verify(groupRightRepository).findAllByGroupId(group.getId());
        Mockito.verify(userTransformer).apply(user);
        Mockito.verify(groupRightTransformer).apply(groupRight);
    }

    @Test
    void testLoadUserByUsername_whenUsernameIsValidAndUserIsNotFound_thenThrowUsernameNotFoundException(){
        // prepare
        String username = "testUsername";
        // when-then
        Mockito.when(userRepository.findByName(username)).thenReturn(null);
        // assert
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
        // verify
        Mockito.verify(userRepository).findByName(username);
        Mockito.verify(groupRightRepository, Mockito.never()).findAllByGroupId(Mockito.anyInt());
    }

    @Test
    void testLoadUserByUsername_whenUsernameIsValidAndGroupRightsIsNotFound_thenThrowRuntimeException(){
        // prepare
        String username = "testUsername";
        User user = new User();
        user.setName(username);
        Group group = new Group();
        group.setId(1);
        user.setGroup(group);
        // when-then
        Mockito.when(userRepository.findByName(username)).thenReturn(user);
        Mockito.when(groupRightRepository.findAllByGroupId(group.getId())).thenReturn(null);
        // assert
        Assertions.assertThrows(RuntimeException.class, () -> userDetailsService.loadUserByUsername(username));
        // verify
        Mockito.verify(userRepository).findByName(username);
        Mockito.verify(groupRightRepository).findAllByGroupId(group.getId());
    }

    @Test
    void testLoadUserByUsername_whenUsernameIsNull_thenThrowUsernameNotFoundException(){
        // when-then
        Mockito.when(userRepository.findByName(null)).thenReturn(null);
        // assert
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(null));
        // verify
        Mockito.verify(userRepository).findByName(null);
    }
}
