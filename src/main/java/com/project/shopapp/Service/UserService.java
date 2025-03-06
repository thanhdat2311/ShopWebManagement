package com.project.shopapp.Service;

import com.project.shopapp.Exception.PermissionDeny;
import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepo;
import com.project.shopapp.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public User updateUser(UserDTO updateUserDTO, Long userId) throws Exception{
        User user = userRepo.findById(userId)
                .orElseThrow(()-> new Exception("User not found!!!"));
        if(!user.getPhone().equals(updateUserDTO.getPhone()) &&
                userRepo.existsByPhone(updateUserDTO.getPhone())){
            throw new DataIntegrityViolationException("Phone number already exists!");
        }
        if (updateUserDTO.getFullName() != null) user.setFullname(updateUserDTO.getFullName());
        if (updateUserDTO.getPhone() != null) user.setPhone(updateUserDTO.getPhone());
        if (updateUserDTO.getAddress() != null) user.setAddress(updateUserDTO.getAddress());
        if (updateUserDTO.getDateOfBirth() != null) user.setDate_of_birth(updateUserDTO.getDateOfBirth());

        // Chuyển đổi int sang boolean
        user.setIs_active(updateUserDTO.getIs_active() == 1);

        // Nếu có role mới, lấy từ DB
        if (updateUserDTO.getRoleId() != null) {
            Role newRole = roleRepo.findById(updateUserDTO.getRoleId())
                    .orElseThrow(() -> new Exception("Role not found!"));
            user.setRole(newRole);
        }
        userRepo.save(user);
        return user;
        // 10:50
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

    @Override
    public User getUserDetails(String token) throws Exception{
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        User user = userRepo.findByPhone(phoneNumber).orElseThrow(
                () ->  new EntityNotFoundException("Cannot find user"));
        return user;
    }
    @Override
    public Boolean validToken(String token){
        if(jwtTokenUtil.isTokenExpired(token)){
            return false;
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepo.findByPhone(phoneNumber);
        if(user.isEmpty()){
            return false;
        }
        return true;
    }
}
