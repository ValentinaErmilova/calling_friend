package com.company.dao;

import com.company.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationSettingDao extends JpaRepository<Setting,String> {
    Setting getFirstBy();
}
