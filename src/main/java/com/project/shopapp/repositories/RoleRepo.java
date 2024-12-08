package com.project.shopapp.repositories;

import com.project.shopapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepo extends JpaRepository<Role, Long> {

}
