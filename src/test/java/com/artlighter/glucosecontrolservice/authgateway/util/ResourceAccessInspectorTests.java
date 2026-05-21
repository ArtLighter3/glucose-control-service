package com.artlighter.glucosecontrolservice.authgateway.util;

import com.artlighter.glucosecontrolservice.authgateway.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(ResourceAccessInspector.class)
public class ResourceAccessInspectorTests {
    @MockitoBean
    private UserService userService;
    @Autowired
    private ResourceAccessInspector resourceAccessInspector;

//    private final String ACCESS_ALL = "ACCESS_ALL";
//    private final String ACCESS_OWN = "ACCESS_OWN";
//    private final String ACCESS_ATTACHED = "ACCESS_ATTACHED";

    @Test
    public void hasAccessToPatientResource_AccessToAdminIsGranted_UserIsAdmin_AlwaysReturnsTrue() {
        Authentication authentication = createAuthentication(0, "ROLE_ADMIN", "ROLE_PATIENT");
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(0,  authentication, false, true));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(1,  authentication, true, true));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(2,  authentication, false, true));

        authentication = createAuthentication(0, "ROLE_ADMIN", "RANDOM_ROLE");
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(0,  authentication, false, true));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(1,  authentication, true, true));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(2,  authentication, false, true));
    }

    @Test
    public void hasAccessToPatientResource_UserHasSuperuserRole_AlwaysReturnsTrue() {
        Authentication authentication = createAuthentication(5, "ROLE_SUPERUSER");
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(0,  authentication, false, true));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(1,  authentication, true, true));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(2,  authentication, false, false));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(3,  authentication, true, false));
    }

    @Test
    public void hasAccessToPatientResource_NoFlags_UserIsOwnerButDoesNotHavePatientRole_ReturnsFalse() {
        Authentication authentication = createAuthentication(0, "ROLE_DOCTOR", "ROLE_ADMIN");
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(0, authentication, false, false));
    }

    @Test
    public void hasAccessToPatientResource_NoFlags_UserHasPatientRole_ReturnsTrueOnlyIfUserIsOwner() {
        Authentication authentication = createAuthentication(1, "ROLE_PATIENT");
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(1, authentication, false, false));
        for (int i = 0; i < 50; i++) {
            if (i == 1) continue;
            assertFalse(resourceAccessInspector
                    .hasAccessToPatientResource(i, authentication, false, false));
        }

        authentication = createAuthentication(2, "ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_ADMIN");
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(2, authentication, false, false));
        for (int i = 0; i < 50; i++) {
            if (i == 2) continue;
            assertFalse(resourceAccessInspector
                    .hasAccessToPatientResource(i, authentication, false, false));
        }
    }

    @Test
    public void hasAccessToPatientResource_DoctorAccessIsNotGranted_UserHasDoctorRoleAndAttachedUsers_ReturnsFalse() {
        Authentication authentication = createAuthentication(0, "ROLE_DOCTOR");
        when(userService.isPatientAttached(0, 1)).thenReturn(true);
        when(userService.isPatientAttached(0, 2)).thenReturn(true);

        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(3, authentication, false, false));
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(1, authentication, false, false));
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(2, authentication, false, false));

        authentication = createAuthentication(0, "ROLE_DOCTOR", "ROLE_ADMIN");
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(3, authentication, false, false));
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(1, authentication, false, false));
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(2, authentication, false, false));
    }

    @Test
    public void hasAccessToPatientResource_DoctorAccessIsGranted_UserIsNotOwnerAndDoesNotHaveDoctorRole_ReturnsFalse() {
        Authentication authentication = createAuthentication(0, "ROLE_PATIENT", "ROLE_ADMIN");
        when(userService.isPatientAttached(0, 1)).thenReturn(true);
        when(userService.isPatientAttached(0, 2)).thenReturn(true);

        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(3, authentication, true, false));
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(1, authentication, true, false));
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(2, authentication, true, false));
    }

    @Test
    public void hasAccessToPatientResource_DoctorAccessIsGranted_UserHasDoctorRoleAndAttachedUsers_ReturnsTrueOnlyIfOwnerIsAttached() {
        Authentication authentication = createAuthentication(0, "ROLE_DOCTOR");
        when(userService.isPatientAttached(0, 1)).thenReturn(true);
        when(userService.isPatientAttached(0, 2)).thenReturn(true);

        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(1, authentication, true, false));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(2, authentication, true, false));

        for (int i = 3; i < 10; i++) {
            assertFalse(resourceAccessInspector
                    .hasAccessToPatientResource(i, authentication, true, false));
        }

        authentication = createAuthentication(0, "ROLE_DOCTOR", "ROLE_ADMIN");
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(1, authentication, true, false));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(2, authentication, true, false));
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(3, authentication, true, false));
    }

    @Test
    public void hasAccessToPatientResource_DoctorAccessIsGranted_UserHasBothPatientAndDoctorRole_ReturnsTrueIfCurrentUserIsOwnerOrIfOwnerIsAttachedToUser() {
        Authentication authentication =
                createAuthentication(10, "ROLE_DOCTOR", "ROLE_PATIENT");
        when(userService.isPatientAttached(10, 1)).thenReturn(true);
        when(userService.isPatientAttached(10, 2)).thenReturn(true);

        for (int i = 3; i < 10; i++) {
            assertFalse(resourceAccessInspector
                    .hasAccessToPatientResource(i, authentication, true, false));
        }
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(1, authentication, true, false));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(2, authentication, true, false));
        assertTrue(resourceAccessInspector
                .hasAccessToPatientResource(10, authentication, true, false));
    }

    @Test
    public void hasAccessToPatientResource_DoctorAccessIsNotGranted_ReturnsFalseEvenIfOwnerIsAttachedToDoctor() {
        Authentication authentication =
                createAuthentication(10, "ROLE_DOCTOR");
        when(userService.isPatientAttached(10, 1)).thenReturn(true);
        when(userService.isPatientAttached(10, 2)).thenReturn(true);

        for (int i = 0; i < 10; i++) {
            assertFalse(resourceAccessInspector
                    .hasAccessToPatientResource(i, authentication, false, false));
        }
    }

    @Test
    public void hasAccessToPatientResource_AuthenticationIsNull_ReturnsFalse() {
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(2, null, true, false));
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(3, null, false, false));
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(12, null, false, false));
        assertFalse(resourceAccessInspector
                .hasAccessToPatientResource(22, null, true, true));
    }

    @Test
    public void isOwnerOfResource_CurrentUserIsNotOwner_ReturnsFalse() {
        Authentication authentication = createAuthentication(10, "ROLE_DOCTOR");
        assertFalse(resourceAccessInspector.isOwnerOfResource(2, authentication));
        assertFalse(resourceAccessInspector.isOwnerOfResource(5, authentication));
        assertFalse(resourceAccessInspector.isOwnerOfResource(11, authentication));

        authentication = createAuthentication(10, "ROLE_PATIENT");
        assertFalse(resourceAccessInspector.isOwnerOfResource(2, authentication));
        assertFalse(resourceAccessInspector.isOwnerOfResource(5, authentication));
        assertFalse(resourceAccessInspector.isOwnerOfResource(11, authentication));

        authentication = createAuthentication(10, "ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_ADMIN");
        assertFalse(resourceAccessInspector.isOwnerOfResource(2, authentication));
        assertFalse(resourceAccessInspector.isOwnerOfResource(5, authentication));
        assertFalse(resourceAccessInspector.isOwnerOfResource(11, authentication));

        authentication = createAuthentication(10);
        assertFalse(resourceAccessInspector.isOwnerOfResource(2, authentication));
        assertFalse(resourceAccessInspector.isOwnerOfResource(5, authentication));
        assertFalse(resourceAccessInspector.isOwnerOfResource(11, authentication));
    }

    @Test
    public void isOwnerOfResource_AuthenticationIsNull_ReturnsFalse() {
        assertFalse(resourceAccessInspector.isOwnerOfResource(2, null));
    }

    @Test
    public void isOwnerOfResource_CurrentUserIsOwner_ReturnsTrue() {
        Authentication authentication = createAuthentication(10, "ROLE_DOCTOR");
        assertTrue(resourceAccessInspector.isOwnerOfResource(10, authentication));

        authentication = createAuthentication(10, "ROLE_PATIENT");
        assertTrue(resourceAccessInspector.isOwnerOfResource(10, authentication));

        authentication = createAuthentication(10, "ROLE_PATIENT", "ROLE_DOCTOR");
        assertTrue(resourceAccessInspector.isOwnerOfResource(10, authentication));

        authentication = createAuthentication(10, "ROLE_ADMIN");
        assertTrue(resourceAccessInspector.isOwnerOfResource(10, authentication));

        authentication = createAuthentication(10, "ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_ADMIN");
        assertTrue(resourceAccessInspector.isOwnerOfResource(10, authentication));

        authentication = createAuthentication(10);
        assertTrue(resourceAccessInspector.isOwnerOfResource(10, authentication));
    }

    private Authentication createAuthentication(int id, String... authorities) {
        Set<GrantedAuthority> authoritiesSet = new HashSet<>();
        for (String authority : authorities) {
            authoritiesSet.add(new SimpleGrantedAuthority(authority));
        }

        ServiceUserDetails userDetails = new ServiceUserDetails(id, "user", "user", authoritiesSet);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), authoritiesSet);
    }

    //    @Test
//    public void hasPermissionForResource_userHasPermissionToAccessAll_AlwaysReturnsTrue() {
//        Authentication authentication = createAuthentication(0, ACCESS_ALL, "Random_Role1", "Random_Role2");
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                0, authentication));
//
//        authentication = createAuthentication(0, ACCESS_ALL, "Random_Role1", "Random_Role2");
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                3, authentication));
//
//        authentication = createAuthentication(0, ACCESS_ALL, ACCESS_ATTACHED);
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                3, authentication));
//
//        authentication = createAuthentication(0, ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN);
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                0, authentication));
//
//        authentication = createAuthentication(0, ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN, "Random_Role1");
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                3, authentication));
//    }
//
//    @Test
//    public void hasPermissionForResource_userHasNoneOfSpecifiedPermissions_ReturnsFalse() {
//        Authentication authentication = createAuthentication(0, "Random_Role1", "Random_Role2");
//        assertFalse(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                0, authentication));
//
//        authentication = createAuthentication(0, "Random_Role1", "Random_Role2");
//        assertFalse(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                3, authentication));
//    }
//
//    @Test
//    public void hasPermissionForResource_userOnlyHasPermissionToAccessOwn_ReturnsTrueIfCurrentUserIsOwner() {
//        Authentication authentication = createAuthentication(2, ACCESS_OWN, "Random_Role1", "Random_Role2");
//
//        for (int i = 0; i < 50; i++) {
//            if (i == 2) continue;
//            assertFalse(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                    i, authentication));
//        }
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                2, authentication));
//    }
//
//    @Test
//    public void hasPermissionForResource_userHasAttachedUsersButNoPermissionToAccessAttached_ReturnsFalseEvenIfOwnerIsAttachedToUser() {
//        Authentication authentication = createAuthentication(0, ACCESS_OWN, "Random_Role1", "Random_Role2");
//        when(doctorProfileService.isPatientAttached(0, 1)).thenReturn(true);
//        when(doctorProfileService.isPatientAttached(0, 2)).thenReturn(true);
//
//        assertFalse(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                1, authentication));
//        assertFalse(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                2, authentication));
//    }
//
//    @Test
//    public void hasPermissionForResource_userHasPermissionToAccessAttached_ReturnsTrueIfOwnerIsAttachedToUser() {
//        Authentication authentication = createAuthentication(0, ACCESS_ATTACHED, "Random_Role1", "Random_Role2");
//        when(doctorProfileService.isPatientAttached(0, 1)).thenReturn(true);
//        when(doctorProfileService.isPatientAttached(0, 2)).thenReturn(true);
//
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                1, authentication));
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                2, authentication));
//
//        for (int i = 3; i < 10; i++) {
//            assertFalse(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                    i, authentication));
//        }
//    }
//
//    @Test
//    public void hasPermissionForResource_userHasPermissionToAccessOwnAndToAccessAttached_ReturnsTrueIfCurrentUserIsOwnerOrIfOwnerIsAttachedToUser() {
//        Authentication authentication =
//                createAuthentication(10, ACCESS_OWN, ACCESS_ATTACHED, "Random_Role1", "Random_Role2");
//        when(doctorProfileService.isPatientAttached(10, 1)).thenReturn(true);
//        when(doctorProfileService.isPatientAttached(10, 2)).thenReturn(true);
//
//        for (int i = 3; i < 10; i++) {
//            assertFalse(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                    i, authentication));
//        }
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                1, authentication));
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                2, authentication));
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
//                10, authentication));
//    }
//
//    @Test
//    public void hasPermissionForResource_ResourceHasOnlyPersonalAccess_ReturnsTrueOnlyIfUserIsOwnerEvenIfHeHasAttachedUsersOrAccessAllAuthority() {
//        Authentication authentication =
//                createAuthentication(10, ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN, "Random_Role1", "Random_Role2");
//        when(doctorProfileService.isPatientAttached(10, 1)).thenReturn(true);
//        when(doctorProfileService.isPatientAttached(10, 2)).thenReturn(true);
//
//        for (int i = 0; i < 10; i++) {
//            assertFalse(resourceAccessInspector.hasPermissionForResourceByAuthorities(null, null, ACCESS_OWN,
//                    i, authentication));
//        }
//        assertTrue(resourceAccessInspector.hasPermissionForResourceByAuthorities(null, null, ACCESS_OWN,
//                10, authentication));
//    }

}
