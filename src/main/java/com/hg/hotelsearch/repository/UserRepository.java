package com.hg.hotelsearch.repository;

import com.hg.hotelsearch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserNameAndUserPassword(String userName, String userPassword);
}
