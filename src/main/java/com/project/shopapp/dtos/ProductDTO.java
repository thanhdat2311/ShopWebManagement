package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private List<MultipartFile> files;
    @Size(min = 3,max = 200,message = "Name must be between 3 and 200 characters")
    private String name;
    @Min(value = 0, message = "Price must be >= 0")
    @Max(value = 1000000, message = "Price must be <= 10MB")
    private Float price;

    private String thumbnail;

    private String description;

    //@JsonProperty("categories_id")
    private Long categories_id;
}
