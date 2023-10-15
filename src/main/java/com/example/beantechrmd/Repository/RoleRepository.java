package com.example.beantechrmd.Repository;

import com.example.beantechrmd.Entity.Role;
import com.example.beantechrmd.Model.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByAuthority(EnumRole authority);

}
