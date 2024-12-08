package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Phone can not be empty!")
    private  String phone;
    @NotBlank(message = "Password can not be empty!")
    private String password;
    @NotNull(message = "Password can not be empty!")
    private Long roleId;
}
