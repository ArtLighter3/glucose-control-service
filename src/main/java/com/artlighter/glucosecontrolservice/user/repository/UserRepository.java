package com.artlighter.glucosecontrolservice.user.repository;

import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
    User findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username=:username")
    User findByUsernameWithRoles(@Param("username") String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id=:id")
    User findByIdWithRoles(@Param("id") int id);
//    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u JOIN u.roles r WHERE u.id = :id AND r. = :value")
//    boolean hasRoles(@Param("id") int id, @Param("roles") Role role);
    boolean existsByIdAndRolesContaining(int id, Role role);

//    @Query("SELECT u FROM User u WHERE u.roles IN :roles")
//    Slice<User> findAllByRolesContaining(@Param("roles") Set<Role> roles, Pageable pageable);
//    Slice<User> findAllByRolesContaining(Role role, Pageable pageable);
    @Query("SELECT u.id FROM User u " +
            "WHERE LOWER(CONCAT(u.lastName, ' ', u.firstName, ' ', COALESCE(u.middleName, ''))) " +
            "LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<Integer> searchUserIDsByFullName(@Param("searchQuery") String searchQuery, Pageable pageable);

    @Query("SELECT u.id FROM User u " +
            "WHERE LOWER(CONCAT(u.lastName, ' ', u.firstName, ' ', COALESCE(u.middleName, ''))) " +
            "LIKE LOWER(CONCAT('%', :searchQuery, '%')) AND :role MEMBER OF u.roles")
    Page<Integer> searchUserIDsByFullNameAndRolesContaining(@Param("searchQuery") String searchQuery,
                                                        @Param("role") Role role, Pageable pageable);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id IN :ids")
    List<User> findAllByIdsWithRoles(@Param("ids") List<Integer> ids, Sort sort);
}
