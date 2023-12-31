package com.example.beantechrmd.Repository;

import com.example.beantechrmd.Entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, Integer> {
    Optional<UserApp> findByUsername(String username);
    Boolean existsByUsername(String username);
}
