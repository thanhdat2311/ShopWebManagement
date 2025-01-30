package com.project.shopapp.response;

import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse
{
    private Long id;
    private String name;

    private Float price;

    private String thumbnail;

    private String description;

    //@JsonProperty("categories_id")
    private Long categories_id;

    private List<ProductImage> product_images = new ArrayList<>();
    public static ProductResponse fromProduct(Product product){
        ProductResponse productResponse =
                ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .thumbnail(product.getThumbnail())
                        .description(product.getDescription())
                        .categories_id(product.getCategory().getId())
                        .product_images(product.getProductImages())
                        .build();

        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
