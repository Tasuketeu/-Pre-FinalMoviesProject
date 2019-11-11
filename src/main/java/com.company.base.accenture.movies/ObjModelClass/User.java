package com.company.base.accenture.movies.ObjModelClass;

import javax.persistence.*;
import java.util.List;

@Entity(name = "User")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name="name")
            private String regName;
    @Column(name="login")
            private String regLogin;
    @Column(name="password")
            private String regPassword;
    @Column(name="admin")
            private String admin;
    @Transient
    private String userInfo;

    public User(String regName, String regLogin, String regPassword, String admin) {
        this.regName = regName;
        this.regLogin = regLogin;
        this.regPassword = regPassword;
        this.admin = admin;
        userInfo = String.format("%s %s %s %s", regName, regLogin, regPassword, admin);
    }

    public User() {

    }
}
