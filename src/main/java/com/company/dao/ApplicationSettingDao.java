package com.company.dao;

import com.company.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationSettingDao extends JpaRepository<Setting,String> {
    Setting getFirstBy();
}
