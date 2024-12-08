package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty("fullname")
    private String fullName;
    @NotBlank(message = "Phone can not be empty!")
    private  String phone;
    @NotBlank(message = "Address can not be empty!")
    private String address;
    @NotBlank(message = "Password can not be empty!")
    @NotBlank(message ="Password can not be empty!")
    private String password;
    private String retypePassword;
    @JsonProperty("facebook_account_id")
    private int facebookAccountID;
    @JsonProperty("google_account_id")
    private int googleAccountID;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("role_id")
    @NotNull(message = "Role can not be empty!")
    private Long roleId;
    private int is_active;

}
