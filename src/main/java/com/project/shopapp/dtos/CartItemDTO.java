package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("quantity")
    private Integer quantity;
}
