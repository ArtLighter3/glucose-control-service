package com.artlighter.glucosecontrolservice.auth.repository.impl;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.repository.AuthorityRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaSelect;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.criteria.spi.CriteriaBuilderExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.*;

/**
 * Реализация репозитория прав для базы данных с использованием ORM
 */
@Repository
public class DatabaseAuthorityRepository implements AuthorityRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DatabaseAuthorityRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Authority addAuthority(Role role, Authority authority, boolean isDeletable) {
        if (role == null || authority == null)
            throw new IllegalArgumentException("Role and/or authority cannot be null");

        RoleAuthorityEntity roleAuthorityEntity =
                new RoleAuthorityEntity(new RoleAuthorityID(role, authority), isDeletable);
       /* RoleAuthorityEntity inDatabaseEntity =
                entityManager.find(RoleAuthorityEntity.class, roleAuthorityEntity.getId());
        if (inDatabaseEntity != null)
            throw new EntityExistsException("Authority already exists for the given role");

        */

        entityManager.persist(roleAuthorityEntity);
        //TODO Почему-то entityManager не закрывается автоматически.
        // Все аннотации на месте (@Transactional у сервиса, @PersistenceContext у менеджера). Почему?
        entityManager.close();

        return authority;
    }

    @Override
    public Authority removeAuthority(Role role, Authority authority) {
        if (role == null || authority == null)
            throw new IllegalArgumentException("Role and/or authority cannot be null");

        RoleAuthorityEntity entity = entityManager.find(RoleAuthorityEntity.class,
                new RoleAuthorityID(role, authority));
        if (entity == null) return null;

        entityManager.remove(entity);
        //TODO Почему-то entityManager не закрывается автоматически.
        // Все аннотации на месте (@Transactional, @PersistenceContext). Почему?
        entityManager.close();
        return authority;
    }

    @Override
    public Map<Authority, Boolean> getRoleAuthorities(Role role) {
        if (role == null) return Collections.<Authority, Boolean>emptyMap();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleAuthorityEntity> query = builder.createQuery(RoleAuthorityEntity.class);
        Root<RoleAuthorityEntity> root = query.from(RoleAuthorityEntity.class);
        query.select(root).where(builder.equal(root.get("id").get("role"), role.name()));

        List<RoleAuthorityEntity> resultList = entityManager.createQuery(query).getResultList();

        Map<Authority, Boolean> authorities = new HashMap<>();
        for (RoleAuthorityEntity row : resultList) {
            authorities.put(row.getId().authority, row.isDeletable());
        }

        return authorities;
    }

    @Entity
    @Table(name = "role_authority")
    private static class RoleAuthorityEntity {
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
    }

    @Embeddable
    private static class RoleAuthorityID implements Serializable {
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
