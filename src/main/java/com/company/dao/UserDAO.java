package com.company.dao;

import com.company.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDAO extends JpaRepository<User,Integer> {

    User findByEmail(String email);

    @Query(value = "select id from users where phonenumber = :number", nativeQuery = true)
    int findIdByPhoneNumber(@Param("number") String phoneNumber);

    User findByPhonenumber(String phoneNumber);
}

