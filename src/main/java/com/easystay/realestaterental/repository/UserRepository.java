package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.User;
import com.easystay.realestaterental.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(UserRole role);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:keyword% OR u.firstName LIKE %:keyword% OR u.lastName LIKE %:keyword%")
    List<User> searchUsers(String keyword);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(UserRole role);
}