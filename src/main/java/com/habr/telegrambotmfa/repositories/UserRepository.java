package com.habr.telegrambotmfa.repositories;

import com.habr.telegrambotmfa.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
