package com.project.shopapp.controllers;

import com.project.shopapp.Service.RolesService;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@RequiredArgsConstructor
public class RolesController {
    private final RolesService rolesService;
    @GetMapping("")
    public ResponseEntity<?> getAllRoles(){
        List<Role> listRoles = rolesService.getAllRoles();
        if (listRoles.isEmpty()){
            return ResponseEntity.badRequest().body("Not Found!");
        }else {
            return  ResponseEntity.ok(listRoles);
        }
    }
}
