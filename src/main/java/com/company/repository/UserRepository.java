package com.company.repository;

import com.company.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//UserDAO
//пакет тоже переименовать в дао
public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findAll();

    @Query(value = "SELECT COUNT(*) FROM users u WHERE u.email = :email and u.password = crypt(:password, u.password)", nativeQuery = true)
    int login(@Param("email") String name, @Param("password") String password);
}
