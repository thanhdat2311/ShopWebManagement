package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotNull(message = "User ID can not be empty")
    private Long user_id;
    @NotBlank(message = "Name can not be empty!")
    private String fullname;
    @NotBlank(message = "Email can not be empty!")
    private String email;
    @NotBlank(message = "Phone can not be empty!")
    private String phone;
    @NotBlank(message = "Address can not be empty!")
    private String address;
    @NotBlank(message = "Note can not be empty!")
    private String note;
    @Min(value = 0, message = "Total Money >= 0")
    private Float total_money;
    private LocalDate shipping_date;
    private String shipping_address;
    private String payment_method;
    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItemDTOList;
}
