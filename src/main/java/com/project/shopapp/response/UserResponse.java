package com.project.shopapp.response;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;

    private String fullName;

    @NotBlank(message = "Phone can not be empty!")
    private  String phone;

    @NotBlank(message = "Address can not be empty!")
    private String address;

    private int facebookAccountID;

    private int googleAccountID;

    private Date dateOfBirth;

    @NotNull(message = "Role can not be empty!")
    private Role role;

    private Boolean is_active;
    public static UserResponse fromUser(User user){
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullname())
                .phone(user.getPhone())
                .address(user.getAddress())
                .dateOfBirth(user.getDate_of_birth())
                .facebookAccountID(user.getFacebook_account_id())
                .googleAccountID(user.getGoogle_account_id())
                .role(user.getRole())
                .is_active(user.getIs_active())
                .build();
    }
}
