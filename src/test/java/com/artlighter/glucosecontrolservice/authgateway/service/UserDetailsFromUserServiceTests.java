package com.artlighter.glucosecontrolservice.authgateway.service;

import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(UserDetailsFromUserService.class)
public class UserDetailsFromUserServiceTests {
    @MockitoBean
    private UserService userService;
//    @MockitoBean
//    private AuthorityService authorityService;
    @Autowired
    private UserDetailsFromUserService userDetailsService;

//    private Set<Authority> patientAuthorities = new HashSet<>();
//    private Set<Authority> adminAuthorities = new HashSet<>();
//    @BeforeEach
//    public void setUp() {
//        //Изначальные условия: роли Больной и Админ содержат по три разных роли
//        patientAuthorities.add(Authority.GLUCOSE_ADD_OWN);
//        patientAuthorities.add(Authority.GLUCOSE_SHOW_OWN);
//        patientAuthorities.add(Authority.GLUCOSE_SHOW_ALL);
//
//        adminAuthorities.add(Authority.GLUCOSE_ADD_ATTACHED);
//        adminAuthorities.add(Authority.GLUCOSE_SHOW_ATTACHED);
//        adminAuthorities.add(Authority.GLUCOSE_ADD_ALL);
//
//        when(authorityService.getAllRoles()).thenReturn(Arrays.stream(Role.values()).collect(Collectors.toSet()));
//        when(authorityService.getAllAuthorities()).thenReturn(Arrays.stream(Authority.values())
//                .collect(Collectors.toSet()));
//        when(authorityService.getRoleAuthorities(eq(Role.ROLE_ADMIN))).thenReturn(adminAuthorities);
//        when(authorityService.getRoleAuthorities(eq(Role.ROLE_PATIENT))).thenReturn(patientAuthorities);
//        when(authorityService.getRoleAuthorities(eq(Role.ROLE_SUPERUSER)))
//                .thenReturn(Arrays.stream(Authority.values()).collect(Collectors.toSet()));
//
//    }

    @Test
    public void loadUserByUsername_CannotFindUser_ThrowsUsernameNotFoundException() {
        when(userService.findUserByUsername(eq("non_existent_user"))).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("non_existent_user"));
    }

    @Test
    public void loadUserByUsername_UserHasNonSuperuserRole_ReturnsUserDetailsWithCorrectGrantedAuthorities() {
        testReturningUserDetails("patient_user", Set.of(Role.ROLE_PATIENT),/* patientAuthorities,*/ false);
        testReturningUserDetails("admin_user", Set.of(Role.ROLE_ADMIN),/* adminAuthorities,*/ false);

        //Set<Authority> mixedAuthorities = new HashSet<>(patientAuthorities);
        //mixedAuthorities.addAll(adminAuthorities);
        testReturningUserDetails("patient_admin_user", Set.of(Role.ROLE_ADMIN, Role.ROLE_PATIENT),
                false);
    }

    @Test
    public void loadUserByUsername_UserHasSuperuserRole_ReturnsUserDetailsWithAllRolesAndAuthorities() {
        testReturningUserDetails("su_user", Set.of(Role.ROLE_SUPERUSER), true);
        testReturningUserDetails("admin_su_user",
                Set.of(Role.ROLE_ADMIN, Role.ROLE_SUPERUSER), true);

    }

    private void testReturningUserDetails(String username, Set<Role> roles, boolean isSuperuser) {
        User user = new User();
        user.setUsername(username);
        user.setRoles(roles);

        when(userService.findUserByUsername(eq(username))).thenReturn(user);

        Set<Role> initialRolesToConstruct = isSuperuser ?
                Arrays.stream(Role.values()).collect(Collectors.toSet()) : roles;

        Set<GrantedAuthority> expectedGrantedAuthorities = initialRolesToConstruct.stream().map(role ->
                new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet());

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertNotNull(userDetails);
        assertSetEquals(expectedGrantedAuthorities, new HashSet<>(userDetails.getAuthorities()));
    }

//    private void testReturningUserDetails(String username, Set<Role> roles, Set<Authority> expectedAuthorities,
//                                          boolean isSuperuser) {
//        User user = new User();
//        user.setUsername(username);
//        user.setRoles(roles);
//
//        when(userService.findUserByUsername(eq(username))).thenReturn(user);
//
//        Set<Role> initialRolesToConstruct = isSuperuser ?
//                Arrays.stream(Role.values()).collect(Collectors.toSet()) : roles;
//        Set<Authority> initialAuthoritiesToConstruct = isSuperuser ?
//                Arrays.stream(Authority.values()).collect(Collectors.toSet()) : expectedAuthorities;
//
//        Set<GrantedAuthority> expectedGrantedAuthorities = initialRolesToConstruct.stream().map(role ->
//                new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet());
//        expectedGrantedAuthorities.addAll(initialAuthoritiesToConstruct.stream().map(authority ->
//                new SimpleGrantedAuthority(authority.name())).collect(Collectors.toSet()));
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        assertNotNull(userDetails);
//        assertIterableEquals(userDetails.getAuthorities().stream().collect(Collectors.toSet()),
//                expectedGrantedAuthorities);
//        //assertI
//        assertSetEquals(expectedGrantedAuthorities, new HashSet<>(userDetails.getAuthorities()));
//    }

    private <T> void assertSetEquals(Set<T> expected, Set<T> actual) {
        assertTrue(expected.size() == actual.size());
        for (T expectedElement : expected) {
            assertTrue(actual.contains(expectedElement));
        }
    }

}
