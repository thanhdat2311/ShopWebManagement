package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties("productImages")
    private Product product;
    @Column(name = "url_image", length = 300)
    private String url_image;
}
