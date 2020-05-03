package com.company.dao;

import com.company.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer> {

    @Query(value = "SELECT u.password FROM users u WHERE u.email = :email", nativeQuery = true)
    String getPasswordHash(@Param("email") String email);

}
