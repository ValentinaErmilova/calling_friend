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

    @Query(value = "SELECT u.password FROM users u WHERE u.email = :email", nativeQuery = true)
    String login(@Param("email") String name);

}
