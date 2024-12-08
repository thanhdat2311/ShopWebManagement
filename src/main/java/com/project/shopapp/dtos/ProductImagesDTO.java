package com.project.shopapp.dtos;

import com.project.shopapp.models.Product;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImagesDTO {
    private Long product_id;
    private String url_image;
}
