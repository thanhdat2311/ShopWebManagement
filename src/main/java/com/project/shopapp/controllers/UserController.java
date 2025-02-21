package com.project.shopapp.controllers;

import com.project.shopapp.Service.UserService;
import com.project.shopapp.dtos.*;
import com.project.shopapp.models.User;
import com.project.shopapp.response.LoginResponse;
import com.project.shopapp.response.UserResponse;
import com.project.shopapp.util.LocalizationUtils;
import com.project.shopapp.util.MessageKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {

            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                // đổi sang báo cả status
                return ResponseEntity.badRequest().body(localizationUtils.getLocalizationMessages(MessageKey.PASSWORD_NOT_MATCH));
            }
            User userRegister = userService.createUser(userDTO);
            if (userRegister == null) {
                return ResponseEntity.badRequest().body("Phone is exist");
            }
            ;
            return ResponseEntity.ok(userRegister);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult result, HttpServletRequest httpServletRequest) {
        try {
            String token = userService.login(userLoginDTO.getPhone(), userLoginDTO.getPassword(), userLoginDTO.getRoleId());
            //return ResponseEntity.ok(Map.of("status", "success", "token", token));
            //Locale locale = RequestContextUtils.getLocale(httpServletRequest);
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(localizationUtils.getLocalizationMessages(MessageKey.LOGIN_SUCCESSFULLY))
                    .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(localizationUtils.getLocalizationMessages(MessageKey.LOGIN_FAILED, e.getMessage()))
                    .build());
            //.status(HttpStatus.BAD_REQUEST)
        }
    }
    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token) {
        try {
            token = token.substring(7);
            User user = userService.getUserDetails(token);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@Valid @PathVariable Long id) {
        if (userService.deleteUser(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not found user!!");
        }
        return ResponseEntity.ok("Delete " + id + " successfully!");
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> getUserDetails(@Valid @RequestBody UserDTO userDTO,
                                            @PathVariable Long userId,
                                            BindingResult result ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            User user = userService.updateUser(userDTO,userId);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
