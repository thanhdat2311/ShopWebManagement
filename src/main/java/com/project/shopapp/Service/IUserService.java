package com.project.shopapp.Service;

import com.project.shopapp.Exception.PermissionDeny;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.models.User;

public interface IUserService {
    User createUser (UserDTO userDTO) throws Exception, PermissionDeny;
    String login(String phone, String password, Long RoleId);
    Long deleteUser(Long id);
    User getUserDetails(String token) throws Exception;
}
