package com.project.shopapp.response;

import com.project.shopapp.models.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse
{
    private String name;

    private Float price;

    private String thumbnail;

    private String description;

    //@JsonProperty("categories_id")
    private Long categories_id;

    public static ProductResponse fromProduct(Product product){
        ProductResponse productResponse =
                ProductResponse.builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .thumbnail(product.getThumbnail())
                        .description(product.getDescription())
                        .categories_id(product.getCategory().getId())
                        .build();

        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
