package com.company.dao;

import com.company.model.MyCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CallDAO extends JpaRepository<MyCall, Integer> {

    @Query(value = "select * from calls where id_to = :id or id_from = :id order by id desc",nativeQuery = true)
    List<MyCall> findCallsById(@Param("id") int id);
}
