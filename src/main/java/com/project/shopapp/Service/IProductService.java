package com.project.shopapp.Service;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImagesDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    List<Product> findProductsByListId(List<Long> listId);
    Product createProduct(ProductDTO productDTO) ;
    Product getProduct(long id);
    Page<ProductResponse> getAllProducts(String keyword,long categoryId,PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO);
    void deleteProduct (long id);
    boolean existsByName(String name);
    ProductImage createProductImage(long productId, ProductImagesDTO productImagesDTO);

}
