//package com.artlighter.glucosecontrolservice.user.repository.impl;
//
//import com.artlighter.glucosecontrolservice.user.entity.Authority;
//import com.artlighter.glucosecontrolservice.user.entity.Role;
//import com.artlighter.glucosecontrolservice.user.repository.AuthorityRepository;
//import jakarta.persistence.*;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Root;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.io.Serializable;
//import java.util.*;
//
///**
// * Реализация репозитория прав для базы данных с использованием ORM
// */
//@Repository
//public class DatabaseAuthorityRepository implements AuthorityRepository {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Autowired
//    public DatabaseAuthorityRepository(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
//
//    /**
//     * @throws IllegalArgumentException в случае, если переданные роль и/или право являются null
//     */
//    @Override
//    public Authority addAuthority(Role role, Authority authority, boolean isDeletable) {
//        if (role == null || authority == null)
//            throw new IllegalArgumentException("Role and/or authority cannot be null");
//
//        RoleAuthorityEntity roleAuthorityEntity =
//                new RoleAuthorityEntity(new RoleAuthorityID(role, authority), isDeletable);
//        RoleAuthorityEntity inDatabaseEntity =
//                entityManager.find(RoleAuthorityEntity.class, roleAuthorityEntity.getId());
//        if (inDatabaseEntity != null) return null;
//
//        entityManager.persist(roleAuthorityEntity);
//
//        entityManager.flush();
//
//        return authority;
//    }
//
//    /**
//     * @throws IllegalArgumentException в случае, если переданные роль и/или право являются null
//     */
//    @Override
//    public Authority removeAuthority(Role role, Authority authority) {
//        if (role == null || authority == null)
//            throw new IllegalArgumentException("Role and/or authority cannot be null");
//
//        RoleAuthorityEntity entity = entityManager.find(RoleAuthorityEntity.class,
//                new RoleAuthorityID(role, authority));
//        if (entity == null) return null;
//
//        entityManager.remove(entity);
//
//        entityManager.flush();
//
//        return authority;
//    }
//
//    @Override
//    public Map<Authority, Boolean> getRoleAuthorities(Role role) {
//        if (role == null) return Collections.<Authority, Boolean>emptyMap();
//
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<RoleAuthorityEntity> query = builder.createQuery(RoleAuthorityEntity.class);
//        Root<RoleAuthorityEntity> root = query.from(RoleAuthorityEntity.class);
//        query.select(root).where(builder.equal(root.get("id").get("role"), role.name()));
//
//        List<RoleAuthorityEntity> resultList = entityManager.createQuery(query).getResultList();
//
//        Map<Authority, Boolean> authorities = new HashMap<>();
//        for (RoleAuthorityEntity row : resultList) {
//            authorities.put(row.getId().authority, row.isDeletable());
//        }
//
//        return authorities;
//    }
//
////    @Override
////    public Set<Role> getRolesOfUser(int userId) {
////        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
////        CriteriaQuery<Ro>
////
////        return Set.of();
////    }
//
//    @Entity
//    @Table(name = "role_authority")
//    private static class RoleAuthorityEntity {
//        @EmbeddedId
//        private RoleAuthorityID id;
//        @Column(name = "is_deletable")
//        private boolean isDeletable;
//
//        public RoleAuthorityEntity() {
//        }
//
//        public RoleAuthorityEntity(RoleAuthorityID id, boolean isDeletable) {
//            this.id = id;
//            this.isDeletable = isDeletable;
//        }
//
//        public RoleAuthorityID getId() {
//            return id;
//        }
//
//        public void setId(RoleAuthorityID id) {
//            this.id = id;
//        }
//
//        public boolean isDeletable() {
//            return isDeletable;
//        }
//
//        public void setDeletable(boolean deletable) {
//            isDeletable = deletable;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (o == null || getClass() != o.getClass()) return false;
//            RoleAuthorityEntity that = (RoleAuthorityEntity) o;
//            return Objects.equals(id, that.id);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hashCode(id);
//        }
//    }
//
//    @Embeddable
//    private static class RoleAuthorityID implements Serializable {
//        @Enumerated(EnumType.STRING)
//        private Role role;
//        @Enumerated(EnumType.STRING)
//        private Authority authority;
//
//        public RoleAuthorityID() {
//        }
//
//        public RoleAuthorityID(Role role, Authority authority) {
//            this.role = role;
//            this.authority = authority;
//        }
//
//        public Role getRole() {
//            return role;
//        }
//
//        public void setRole(Role role) {
//            this.role = role;
//        }
//
//        public Authority getAuthority() {
//            return authority;
//        }
//
//        public void setAuthority(Authority authority) {
//            this.authority = authority;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (o == null || getClass() != o.getClass()) return false;
//            RoleAuthorityID that = (RoleAuthorityID) o;
//            return role == that.role && authority == that.authority;
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(role, authority);
//        }
//    }
//}
