package com.company.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "setting")
public class Setting {
    @Id
    @Column(name = "account_sid")
    private String accountSid;

    @Column(name = "auth_token")
    private String authToken;

    @Column(name = "application_sid")
    private String applicationSid;

    public String getAccountSid() {
        return accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getApplicationSid() {
        return applicationSid;
    }
}
