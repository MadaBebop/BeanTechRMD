package com.example.beantechrmd.Repositories;

import com.example.beantechrmd.Entities.Role;
import com.example.beantechrmd.Models.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByAuthority(EnumRole authority);
}
