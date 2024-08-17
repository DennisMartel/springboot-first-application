package com.martel.curso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.martel.curso.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
