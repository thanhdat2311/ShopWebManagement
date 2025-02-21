package com.project.shopapp.repositories;

import com.project.shopapp.models.Role;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

}
