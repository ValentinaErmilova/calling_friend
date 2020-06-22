package com.company.dao;

import com.company.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer> {

    User findByEmail(String email);

    @Query(value = "select id from users where phonenumber = :number", nativeQuery = true)
    int findIdByPhoneNumber(@Param("number") String phoneNumber);

    User findByPhonenumber(String phoneNumber);

    @Query(value = "select u.id, u.firstname, u.age, u.email, u.password, u.lastname, u.phonenumber " +
            "from users u join friends f on f.friend_id = u.id where f.user_id =:id", nativeQuery = true)
    List<User> getFriends(@Param("id") Integer id);

    int countByPhonenumber(String phoneUmber);

    int countByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "delete from friends where user_id =:userId and friend_id =:friendId", nativeQuery = true)
    void deleteFriend(@Param("userId") int userId, @Param("friendId") int friendId);

    @Modifying
    @Transactional
    @Query(value = "insert into friends values(:userId,:friendId)", nativeQuery = true)
    void addFriend(int userId, int friendId);


}

