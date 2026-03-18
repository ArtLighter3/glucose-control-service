package com.artlighter.glucosecontrolservice.user.repository.impl;

import com.artlighter.glucosecontrolservice.user.entity.Authority;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@DataJpaTest
@Import(DatabaseAuthorityRepository.class)
public class DatabaseAuthorityRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private DatabaseAuthorityRepository repository;

    List<RoleAuthorityEntity> initialOnes = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        initialOnes.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_SHOW_OWN),
                false));
        initialOnes.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_ADD_OWN),
                false));
        for (RoleAuthorityEntity roleAuthority : initialOnes) {
            entityManager.persist(roleAuthority);
        }
        entityManager.flush();
    }

    @AfterEach
    public void tearDown() {
        for (RoleAuthorityEntity roleAuthority : initialOnes) {
            entityManager.remove(roleAuthority);
        }
    }

    @Test
    public void addAuthority_GivenAuthorityIsNonExistentForThisRole_AddsAuthorityToDataSourceAndReturnsGivenAuthority() {
        //Сущности к добавлению
        List<RoleAuthorityEntity> expectedList = new ArrayList<>();
        expectedList.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_SHOW_ALL),
                true));
        expectedList.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_SHOW_ATTACHED),
                false));
        expectedList.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_ADMIN, Authority.GLUCOSE_ADD_OWN),
                true));
        expectedList.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_ADMIN, Authority.GLUCOSE_SHOW_ALL),
                false));
        expectedList.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_DOCTOR, Authority.GLUCOSE_ADD_ATTACHED),
                true));

        //Вызов метода и проверка возвращаемого типа
        for (RoleAuthorityEntity roleAuthority : expectedList) {
            Authority added = repository.addAuthority(roleAuthority.getId().getRole(),
                    roleAuthority.getId().getAuthority(),
                    roleAuthority.isDeletable());

            assertNotNull(added);
            assertEquals(roleAuthority.getId().authority, added);
        }

        //Проверка содержимого хранилища
        Set<RoleAuthorityEntity> all = findAll(RoleAuthorityEntity.class);
        assertEquals(expectedList.size() + initialOnes.size(), all.size());
        for (RoleAuthorityEntity roleAuthority : expectedList) {
            RoleAuthorityEntity saved = entityManager.find(RoleAuthorityEntity.class, roleAuthority.getId());
            assertNotNull(saved);
            assertEquals(roleAuthority.getId().getRole(), saved.getId().getRole());
            assertEquals(roleAuthority.getId().getAuthority(), saved.getId().getAuthority());
            assertEquals(roleAuthority.isDeletable(), saved.isDeletable());
        }
    }

    @Test
    public void addAuthority_GivenAuthoritiesAlreadyExist_ReturnsNullForEveryAttemptAndDoesNotAddThem() {
        //список на добавление
        List<RoleAuthorityEntity> listToCheck = new ArrayList<>();
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_SHOW_OWN),
                false));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_ADD_OWN),
                false));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_SHOW_OWN),
                true));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_ADD_OWN),
                true));
        //Ожидаемое множество
        Set<RoleAuthorityEntity> expected = new HashSet<>(initialOnes);

        //Вызов метода и проверка возвращаемого типа
        for (RoleAuthorityEntity roleAuthority : listToCheck) {
            Authority added = repository.addAuthority(roleAuthority.getId().getRole(),
                    roleAuthority.getId().getAuthority(),
                    roleAuthority.isDeletable());

            assertNull(added);
        }

        //Проверка содержимого хранилища
        Set<RoleAuthorityEntity> all = findAll(RoleAuthorityEntity.class);
        assertEquals(expected.size(), all.size());
        assertIterableEquals(expected, all);
    }

    @Test
    public void addAuthority_GivenRoleOrAuthorityIsNull_ThrowsIllegalArgumentException() {
        List<RoleAuthorityEntity> listToCheck = new ArrayList<>();
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(null, Authority.GLUCOSE_SHOW_ATTACHED),
                        true));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, null), false));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(null, null), true));

        for (RoleAuthorityEntity roleAuthority : listToCheck) {
            assertThrows(IllegalArgumentException.class, () -> {repository.addAuthority(roleAuthority.getId().getRole(),
                    roleAuthority.getId().getAuthority(), roleAuthority.isDeletable());});
        }
    }

    @Test
    public void removeAuthority_GivenAuthoritiesExist_RemovesAuthorityAndReturnsRemovedAuthority() {
        List<RoleAuthorityEntity> listToCheck = new ArrayList<>();
        for (RoleAuthorityEntity roleAuthority : initialOnes) {
            RoleAuthorityEntity existing = entityManager.find(RoleAuthorityEntity.class, roleAuthority.getId());
            assertNotNull(existing);
            listToCheck.add(existing);
        }
        Set<RoleAuthorityEntity> expected = new HashSet<>(initialOnes);

        for (RoleAuthorityEntity roleAuthority : listToCheck) {
            Authority removed = repository.removeAuthority(roleAuthority.getId().getRole(),
                    roleAuthority.getId().getAuthority());
            expected.remove(roleAuthority);

            assertNotNull(removed);
            assertEquals(roleAuthority.getId().authority, removed);
        }

        Set<RoleAuthorityEntity> all = findAll(RoleAuthorityEntity.class);
        assertIterableEquals(expected, all);
    }

    @Test
    public void removeAuthority_GivenAuthoritiesDoNotExist_ReturnsNullForEveryAttemptAndDoesNotRemoveAnything() {
        List<RoleAuthorityEntity> listToCheck = new ArrayList<>();
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_SHOW_ALL),
                true));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_SHOW_ATTACHED),
                false));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_ADMIN, Authority.GLUCOSE_ADD_OWN),
                true));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_ADMIN, Authority.GLUCOSE_SHOW_ALL),
                false));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_DOCTOR, Authority.GLUCOSE_ADD_ATTACHED),
                true));
        Set<RoleAuthorityEntity> expected = new HashSet<>(initialOnes);

        for (RoleAuthorityEntity roleAuthority : listToCheck) {
            Authority removed = repository.removeAuthority(roleAuthority.getId().getRole(),
                    roleAuthority.getId().getAuthority());

            assertNull(removed);
        }

        Set<RoleAuthorityEntity> all = findAll(RoleAuthorityEntity.class);
        assertIterableEquals(expected, all);
    }

    @Test
    public void removeAuthority_GivenRoleOrAuthorityIsNull_ThrowsIllegalArgumentException() {
        List<RoleAuthorityEntity> listToCheck = new ArrayList<>();
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(null, Authority.GLUCOSE_SHOW_ATTACHED),
                true));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, null), false));
        listToCheck.add(new RoleAuthorityEntity(new RoleAuthorityID(null, null), true));

        for (RoleAuthorityEntity roleAuthority : listToCheck) {
            assertThrows(IllegalArgumentException.class, () ->
                    repository.removeAuthority(roleAuthority.getId().getRole(), roleAuthority.getId().getAuthority()));
        }
    }

    @Test
    public void getRoleAuthorities_GivenRoleIsNotNull_ReturnsCorrectAuthoritiesOfRole() {
        //Сущности для добавления
        List<RoleAuthorityEntity> listToAdd = new ArrayList<>();
        listToAdd.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_SHOW_ALL),
                true));
        listToAdd.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_PATIENT, Authority.GLUCOSE_SHOW_ATTACHED),
                false));
        listToAdd.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_ADMIN, Authority.GLUCOSE_ADD_OWN),
                true));
        listToAdd.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_ADMIN, Authority.GLUCOSE_SHOW_ALL),
                false));
        listToAdd.add(new RoleAuthorityEntity(new RoleAuthorityID(Role.ROLE_DOCTOR, Authority.GLUCOSE_ADD_ATTACHED),
                true));
        //Ожидаемые списки
        Map<Authority, Boolean> expectedForPatient = new HashMap<>(),
                expectedForAdmin = new HashMap<>(),
                expectedForDoctor = new HashMap<>();
        for (RoleAuthorityEntity roleAuthority : initialOnes)
            expectedForPatient.put(roleAuthority.getId().getAuthority(), roleAuthority.isDeletable);
        for (RoleAuthorityEntity roleAuthority : listToAdd) {
            entityManager.persist(roleAuthority);
            switch (roleAuthority.getId().getRole()) {
                case ROLE_PATIENT:
                    expectedForPatient.put(roleAuthority.getId().getAuthority(), roleAuthority.isDeletable);
                    break;
                case ROLE_ADMIN:
                    expectedForAdmin.put(roleAuthority.getId().getAuthority(), roleAuthority.isDeletable);
                    break;
                default:
                    expectedForDoctor.put(roleAuthority.getId().getAuthority(), roleAuthority.isDeletable);
            }
        }
        entityManager.flush();

        //Вызов метода и проверка
        Map<Authority, Boolean> actualForPatient = repository.getRoleAuthorities(Role.ROLE_PATIENT);
        assertMapEquals(expectedForPatient, actualForPatient);
        Map<Authority, Boolean> actualForAdmin = repository.getRoleAuthorities(Role.ROLE_ADMIN);
        assertMapEquals(expectedForAdmin, actualForAdmin);
        Map<Authority, Boolean> actualForDoctor = repository.getRoleAuthorities(Role.ROLE_DOCTOR);
        assertMapEquals(expectedForDoctor, actualForDoctor);
    }

    @Test
    public void getRoleAuthorities_GivenRoleIsNull_ReturnsEmptyCollection() {
        Map<Authority, Boolean> map = repository.getRoleAuthorities(null);
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    private <T> Set<T> findAll(Class<T> tClass) {
        CriteriaBuilder builder = entityManager.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(tClass);
        Root<T> root = query.from(tClass);
        query.select(root);

        return entityManager.getEntityManager().createQuery(query).getResultStream().collect(Collectors.toSet());
    }

    private <K, V> void assertMapEquals(Map<K, V> expected, Map<K, V> actual) {
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<K, V> entry : expected.entrySet()) {
            V actualValue = actual.get(entry.getKey());
            assertNotNull(actualValue);
            assertEquals(entry.getValue(), actualValue);
        }
    }

    @Entity
    @Table(name = "role_authority")
    public static class RoleAuthorityEntity {
        @EmbeddedId
        private RoleAuthorityID id;
        @Column(name = "is_deletable")
        private boolean isDeletable;

        public RoleAuthorityEntity() {
        }

        public RoleAuthorityEntity(RoleAuthorityID id, boolean isDeletable) {
            this.id = id;
            this.isDeletable = isDeletable;
        }

        public RoleAuthorityID getId() {
            return id;
        }

        public void setId(RoleAuthorityID id) {
            this.id = id;
        }

        public boolean isDeletable() {
            return isDeletable;
        }

        public void setDeletable(boolean deletable) {
            isDeletable = deletable;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            RoleAuthorityEntity that = (RoleAuthorityEntity) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }

    @Embeddable
    public static class RoleAuthorityID implements Serializable {
        @Enumerated(EnumType.STRING)
        private Role role;
        @Enumerated(EnumType.STRING)
        private Authority authority;

        public RoleAuthorityID() {
        }

        public RoleAuthorityID(Role role, Authority authority) {
            this.role = role;
            this.authority = authority;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public Authority getAuthority() {
            return authority;
        }

        public void setAuthority(Authority authority) {
            this.authority = authority;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            RoleAuthorityID that = (RoleAuthorityID) o;
            return role == that.role && authority == that.authority;
        }

        @Override
        public int hashCode() {
            return Objects.hash(role, authority);
        }
    }
}
