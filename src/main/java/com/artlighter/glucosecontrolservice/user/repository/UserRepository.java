package com.artlighter.glucosecontrolservice.user.repository;

import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

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
}
