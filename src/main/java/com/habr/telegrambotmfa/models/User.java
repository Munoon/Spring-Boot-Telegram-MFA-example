package com.habr.telegrambotmfa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public enum Role implements GrantedAuthority {
        ROLE_USER, ROLE_ADMIN;

        @Override
        public String getAuthority() {
            return name();
        }
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
