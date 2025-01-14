package com.project.shopapp.Service;

import com.project.shopapp.Exception.PermissionDeny;
import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepo;
import com.project.shopapp.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.security.InvalidParameterException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        if(!userRepo.existsByPhone(userDTO.getPhone())){
            Role role = roleRepo.findById(userDTO.getRoleId()).orElseThrow(
                    () -> new IllegalArgumentException("không tìm thấy role"));
            if(role.getName().equals(Role.ADMIN)){
            throw new InvalidParameterException("mày không thể tạo acc admin được thằng nhóc à!");

            }
        User user = User.builder().fullname(userDTO.getFullName())
                .phone(userDTO.getPhone()).address(userDTO.getAddress()).date_of_birth(userDTO.getDateOfBirth())
                .facebook_account_id(userDTO.getFacebookAccountID())
                .google_account_id(userDTO.getGoogleAccountID())
                .build();
        user.setRole(role);
        if(userDTO.getFacebookAccountID() == 0 && userDTO.getGoogleAccountID()==0){
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
        }
        userRepo.save(user);
        return user;}
        return null;

    }

    @Override
    public String login(String phone, String password, Long RoleId) {
        Optional<User> userOptional = userRepo.findByPhone(phone);
        if (userOptional.isEmpty()){
            throw new IllegalArgumentException("Invalid phone number/password");
        }
        User user = userOptional.get();
        if(user.getFacebook_account_id() == 0 && user.getGoogle_account_id()==0) {
            if(!passwordEncoder.matches(password,user.getPassword())){
                throw new BadCredentialsException("Wrong phone numer or password");
            }
        }
        if(user.getRole().getId() != RoleId){
            throw new BadCredentialsException("Wrong Role!");
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(phone, password,user.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return jwtTokenUtil.generateToken(user);
    }

    @Override
    public Long deleteUser(Long id) {
        if (userRepo.existsById(id)){
            userRepo.deleteById(id);
            return id;
        }
        return null;
    }
}
