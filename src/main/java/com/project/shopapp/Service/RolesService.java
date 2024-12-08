package com.project.shopapp.Service;

import com.project.shopapp.models.Role;
import com.project.shopapp.repositories.RoleRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class RolesService implements IRolesService{
    RoleRepo roleRepo;
    @Override
    public List<Role> getAllRoles() {
        List<Role> listRole = roleRepo.findAll();
        if(!listRole.isEmpty()){
            return listRole;
        }else{
            return null;
        }
    }
}
