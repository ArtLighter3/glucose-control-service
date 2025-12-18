package com.artlighter.glucosecontrolservice.auth.service;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.repository.AuthorityRepository;
import com.artlighter.glucosecontrolservice.auth.util.exception.AuthorityIsNotDeletableException;
import com.artlighter.glucosecontrolservice.auth.util.exception.RoleAlreadyHasAuthorityException;
import com.artlighter.glucosecontrolservice.auth.util.exception.RoleDoesNotHaveSuchAuthorityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

//TODO Сократить бойлерплейт код
@ExtendWith(SpringExtension.class)
@Import(AuthorityService.class)
public class AuthorityServiceTests {
    @MockitoBean
    private AuthorityRepository repository;
    @Autowired
    private AuthorityService service;

    private Map<Authority, Boolean> patientAuthorities = new HashMap<>();
    private Map<Authority, Boolean> adminAuthorities = new HashMap<>();
    @BeforeEach
    public void setUp() {
        //Изначальные условия: роли Больной и Админ содержат по три разных роли
        patientAuthorities.put(Authority.GLUCOSE_ADD_OWN, false);
        patientAuthorities.put(Authority.GLUCOSE_SHOW_OWN, false);
        patientAuthorities.put(Authority.GLUCOSE_SHOW_ALL, true);
        for (Authority authority : patientAuthorities.keySet()) {
            when(repository.addAuthority(eq(Role.ROLE_ADMIN), eq(authority), anyBoolean()))
                    .thenReturn(authority);
            when(repository.addAuthority(eq(Role.ROLE_PATIENT), eq(authority), anyBoolean()))
                    .thenReturn(null);
            when(repository.removeAuthority(eq(Role.ROLE_ADMIN), eq(authority)))
                    .thenReturn(null);
            when(repository.removeAuthority(eq(Role.ROLE_PATIENT), eq(authority)))
                    .thenReturn(authority);
        }

        adminAuthorities.put(Authority.GLUCOSE_ADD_ATTACHED, true);
        adminAuthorities.put(Authority.GLUCOSE_SHOW_ATTACHED, true);
        adminAuthorities.put(Authority.GLUCOSE_ADD_ALL, false);
        for (Authority authority : adminAuthorities.keySet()) {
            when(repository.addAuthority(eq(Role.ROLE_ADMIN), eq(authority), anyBoolean()))
                    .thenReturn(null);
            when(repository.addAuthority(eq(Role.ROLE_PATIENT), eq(authority), anyBoolean()))
                    .thenReturn(authority);
            when(repository.removeAuthority(eq(Role.ROLE_ADMIN), eq(authority)))
                    .thenReturn(authority);
            when(repository.removeAuthority(eq(Role.ROLE_PATIENT), eq(authority)))
                    .thenReturn(null);
        }

        when(repository.getRoleAuthorities(Role.ROLE_PATIENT)).thenReturn(patientAuthorities);
        when(repository.getRoleAuthorities(Role.ROLE_ADMIN)).thenReturn(adminAuthorities);
    }

    @Test
    public void addAuthority_GivenRoleOrAuthorityIsNull_ReturnsNull() {
        Authority added = service.addAuthority(null, Authority.GLUCOSE_ADD_OWN, false);
        assertNull(added);

        added = service.addAuthority(Role.ROLE_PATIENT, null, false);
        assertNull(added);

        added = service.addAuthority(null, null, true);
        assertNull(added);
    }

    @Test
    public void addAuthority_GivenAuthorityDoesNotExistInGivenRole_CallsRepositoryAndReturnsAddedAuthority() {
        for (Map.Entry<Authority, Boolean> entry : patientAuthorities.entrySet()) {
            Authority authority = entry.getKey();
            boolean isDeletable = entry.getValue();
            Authority added = service.addAuthority(Role.ROLE_ADMIN, authority, isDeletable);
            assertNotNull(added);
            assertEquals(authority, added);
        }
        verify(repository, times(patientAuthorities.size())).addAuthority(any(), any(), anyBoolean());
    }

    @Test
    public void addAuthority_GivenAuthorityExistsInGivenRole_ThrowsRoleAlreadyHasAuthorityException() {
        for (Map.Entry<Authority, Boolean> entry : patientAuthorities.entrySet()) {
            Authority authority = entry.getKey();
            boolean isDeletable = entry.getValue();
            assertThrows(RoleAlreadyHasAuthorityException.class, () ->
                    service.addAuthority(Role.ROLE_PATIENT, authority, isDeletable));
        }
    }

    @Test
    public void addDeletableAuthority_GivenRoleOrAuthorityIsNull_ReturnsNull() {
        Authority added = service.addDeletableAuthority(null, Authority.GLUCOSE_ADD_OWN);
        assertNull(added);

        added = service.addDeletableAuthority(Role.ROLE_PATIENT, null);
        assertNull(added);

        added = service.addDeletableAuthority(null, null);
        assertNull(added);
    }

    @Test
    public void addDeletableAuthority_GivenAuthorityDoesNotExistInGivenRole_CallsRepositoryAndReturnsAddedAuthority() {
        for (Map.Entry<Authority, Boolean> entry : patientAuthorities.entrySet()) {
            Authority expected = entry.getKey();
            Authority added = service.addDeletableAuthority(Role.ROLE_ADMIN, expected);
            assertNotNull(added);
            assertEquals(expected, added);
        }
        verify(repository, times(patientAuthorities.size())).addAuthority(any(), any(), eq(true));
    }

    @Test
    public void addDeletableAuthority_GivenAuthorityExistsInGivenRole_ThrowsRoleAlreadyHasAuthorityException() {
        for (Map.Entry<Authority, Boolean> entry : patientAuthorities.entrySet()) {
            Authority authority = entry.getKey();
            assertThrows(RoleAlreadyHasAuthorityException.class, () ->
                    service.addDeletableAuthority(Role.ROLE_PATIENT, authority));
        }
    }

    @Test
    public void addUndeletableAuthority_GivenRoleOrAuthorityIsNull_ReturnsNull() {
        Authority added = service.addUndeletableAuthority(null, Authority.GLUCOSE_ADD_OWN);
        assertNull(added);

        added = service.addUndeletableAuthority(Role.ROLE_PATIENT, null);
        assertNull(added);

        added = service.addUndeletableAuthority(null, null);
        assertNull(added);
    }

    @Test
    public void addUndeletableAuthority_GivenAuthorityDoesNotExistInGivenRole_CallsRepositoryAndReturnsAddedAuthority() {
        for (Map.Entry<Authority, Boolean> entry : patientAuthorities.entrySet()) {
            Authority expected = entry.getKey();
            Authority added = service.addUndeletableAuthority(Role.ROLE_ADMIN, expected);
            assertNotNull(added);
            assertEquals(expected, added);
        }
        verify(repository, times(patientAuthorities.size())).addAuthority(any(), any(), eq(false));
    }

    @Test
    public void addUndeletableAuthority_GivenAuthorityExistsInGivenRole_ThrowsRoleAlreadyHasAuthorityException() {
        for (Map.Entry<Authority, Boolean> entry : patientAuthorities.entrySet()) {
            Authority authority = entry.getKey();
            assertThrows(RoleAlreadyHasAuthorityException.class, () ->
                    service.addUndeletableAuthority(Role.ROLE_PATIENT, authority));
        }
    }

    @Test
    public void removeAuthority_GivenRoleOrAuthorityIsNull_ReturnsNull() {
        Authority removed = service.removeAuthority(null, Authority.GLUCOSE_ADD_OWN);
        assertNull(removed);

        removed = service.removeAuthority(Role.ROLE_PATIENT, null);
        assertNull(removed);

        removed = service.removeAuthority(null, null);
        assertNull(removed);
    }

    @Test
    public void removeAuthority_GivenAuthorityDoesNotExistInGivenRole_ThrowsRoleDoesNotHaveSuchAuthorityException() {
        for (Map.Entry<Authority, Boolean> entry : patientAuthorities.entrySet()) {
            assertThrows(RoleDoesNotHaveSuchAuthorityException.class, () ->
                    service.removeAuthority(Role.ROLE_ADMIN, entry.getKey()));
        }
    }

    @Test
    public void removeAuthority_GivenAuthorityExistsInGivenRoleButIsNotDeletable_ThrowsAuthorityIsNotDeletableException() {
        for (Map.Entry<Authority, Boolean> entry : patientAuthorities.entrySet()) {
            if (entry.getValue()) continue;
            assertThrows(AuthorityIsNotDeletableException.class, () ->
                    service.removeAuthority(Role.ROLE_PATIENT, entry.getKey()));
        }
    }

    @Test
    public void removeAuthority_GivenAuthorityExistsInGivenRoleAndIsDeletable_CallsRepositoryAndReturnsRemovedAuthority() {
        int deletable = 0;
        for (Map.Entry<Authority, Boolean> entry : patientAuthorities.entrySet()) {
            if (!entry.getValue()) continue;
            deletable++;
            Authority removed = service.removeAuthority(Role.ROLE_PATIENT, entry.getKey());
            assertNotNull(removed);
            assertEquals(entry.getKey(), removed);
        }
        verify(repository, times(deletable)).removeAuthority(isNotNull(), isNotNull());
    }

    @Test
    public void getRoleAuthorities_GivenRoleIsCorrect_ReturnsCorrectSetOfAuthorities() {
        Set<Authority> expectedPatientAuthorities = new HashSet<>(patientAuthorities.keySet());
        Set<Authority> expectedAdminAuthorities = new HashSet<>(adminAuthorities.keySet());

        Set<Authority> actualPatientAuthorities = service.getRoleAuthorities(Role.ROLE_PATIENT);
        assertIterableEquals(expectedPatientAuthorities, actualPatientAuthorities);
        Set<Authority> actualAdminAuthorities = service.getRoleAuthorities(Role.ROLE_ADMIN);
        assertIterableEquals(expectedAdminAuthorities, actualAdminAuthorities);
    }

    @Test
    public void getRoleAuthorities_GivenRoleIsSuperuser_ReturnsAllAuthorities() {
        Set<Authority> expectedSuperuserAuthorities = new HashSet<>(List.of(Authority.values()));

        Set<Authority> actualSuperuserAuthorities = service.getRoleAuthorities(Role.ROLE_SUPERUSER);
        assertIterableEquals(expectedSuperuserAuthorities, actualSuperuserAuthorities);
    }

    @Test
    public void getRoleAuthorities_RoleIsNull_ReturnsEmptySet() {
        Set<Authority> actual = service.getRoleAuthorities(null);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void getRoleAuthorities_RepositoryReturnsNull_ReturnsEmptySet() {
        when(repository.getRoleAuthorities(any())).thenReturn(null);
        Set<Authority> actual = service.getRoleAuthorities(Role.ROLE_PATIENT);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }
}
