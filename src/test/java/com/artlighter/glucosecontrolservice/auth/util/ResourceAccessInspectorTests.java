package com.artlighter.glucosecontrolservice.auth.util;

import com.artlighter.glucosecontrolservice.auth.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.user.service.DoctorProfileService;
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
    private DoctorProfileService doctorProfileService;
    @Autowired
    private ResourceAccessInspector resourceAccessInspector;

    private final String ACCESS_ALL = "ACCESS_ALL";
    private final String ACCESS_OWN = "ACCESS_OWN";
    private final String ACCESS_ATTACHED = "ACCESS_ATTACHED";

    @Test
    public void hasPermissionForResource_userHasPermissionToAccessAll_AlwaysReturnsTrue() {
        Authentication authentication = createAuthentication(0, ACCESS_ALL, "Random_Role1", "Random_Role2");
        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                0, authentication));

        authentication = createAuthentication(0, ACCESS_ALL, "Random_Role1", "Random_Role2");
        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                3, authentication));

        authentication = createAuthentication(0, ACCESS_ALL, ACCESS_ATTACHED);
        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                3, authentication));

        authentication = createAuthentication(0, ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN);
        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                0, authentication));

        authentication = createAuthentication(0, ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN, "Random_Role1");
        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                3, authentication));
    }

    @Test
    public void hasPermissionForResource_userHasNoneOfSpecifiedPermissions_ReturnsFalse() {
        Authentication authentication = createAuthentication(0, "Random_Role1", "Random_Role2");
        assertFalse(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                0, authentication));

        authentication = createAuthentication(0, "Random_Role1", "Random_Role2");
        assertFalse(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                3, authentication));
    }

    @Test
    public void hasPermissionForResource_userOnlyHasPermissionToAccessOwn_ReturnsTrueIfCurrentUserIsOwner() {
        Authentication authentication = createAuthentication(2, ACCESS_OWN, "Random_Role1", "Random_Role2");

        for (int i = 0; i < 50; i++) {
            if (i == 2) continue;
            assertFalse(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                    i, authentication));
        }
        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                2, authentication));
    }

    @Test
    public void hasPermissionForResource_userHasAttachedUsersButNoPermissionToAccessAttached_ReturnsFalseEvenIfOwnerIsAttachedToUser() {
        Authentication authentication = createAuthentication(0, ACCESS_OWN, "Random_Role1", "Random_Role2");
        when(doctorProfileService.isPatientAttached(0, 1)).thenReturn(true);
        when(doctorProfileService.isPatientAttached(0, 2)).thenReturn(true);

        assertFalse(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                1, authentication));
        assertFalse(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                2, authentication));
    }

    @Test
    public void hasPermissionForResource_userHasPermissionToAccessAttached_ReturnsTrueIfOwnerIsAttachedToUser() {
        Authentication authentication = createAuthentication(0, ACCESS_ATTACHED, "Random_Role1", "Random_Role2");
        when(doctorProfileService.isPatientAttached(0, 1)).thenReturn(true);
        when(doctorProfileService.isPatientAttached(0, 2)).thenReturn(true);

        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                1, authentication));
        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                2, authentication));

        for (int i = 3; i < 10; i++) {
            assertFalse(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                    i, authentication));
        }
    }

    @Test
    public void hasPermissionForResource_userHasPermissionToAccessOwnAndToAccessAttached_ReturnsTrueIfCurrentUserIsOwnerOrIfOwnerIsAttachedToUser() {
        Authentication authentication =
                createAuthentication(10, ACCESS_OWN, ACCESS_ATTACHED, "Random_Role1", "Random_Role2");
        when(doctorProfileService.isPatientAttached(10, 1)).thenReturn(true);
        when(doctorProfileService.isPatientAttached(10, 2)).thenReturn(true);

        for (int i = 3; i < 10; i++) {
            assertFalse(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                    i, authentication));
        }
        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                1, authentication));
        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                2, authentication));
        assertTrue(resourceAccessInspector.hasPermissionForResource(ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN,
                10, authentication));
    }

    @Test
    public void hasPermissionForResource_ResourceHasOnlyPersonalAccess_ReturnsTrueOnlyIfUserIsOwnerEvenIfHeHasAttachedUsersOrAccessAllAuthority() {
        Authentication authentication =
                createAuthentication(10, ACCESS_ALL, ACCESS_ATTACHED, ACCESS_OWN, "Random_Role1", "Random_Role2");
        when(doctorProfileService.isPatientAttached(10, 1)).thenReturn(true);
        when(doctorProfileService.isPatientAttached(10, 2)).thenReturn(true);

        for (int i = 0; i < 10; i++) {
            assertFalse(resourceAccessInspector.hasPermissionForResource(null, null, ACCESS_OWN,
                    i, authentication));
        }
        assertTrue(resourceAccessInspector.hasPermissionForResource(null, null, ACCESS_OWN,
                10, authentication));
    }

    private Authentication createAuthentication(int id, String... authorities) {
        Set<GrantedAuthority> authoritiesSet = new HashSet<>();
        for (String authority : authorities) {
            authoritiesSet.add(new SimpleGrantedAuthority(authority));
        }

        ServiceUserDetails userDetails = new ServiceUserDetails(id, "user", "user", authoritiesSet);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), authoritiesSet);
    }
}
