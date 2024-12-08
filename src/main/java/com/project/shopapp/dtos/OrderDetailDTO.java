package com.project.shopapp.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @NotNull(message = "Order Id can be not empty")
    private Long order_id;
    private Long product_id;
    @Min(value = 0, message = "Price must be >=0")
    private float price;
    private Long number_of_product;
    @Min(value = 0,message = "Total Money must be >=0")
    private float total_money;
    private String color;

}
