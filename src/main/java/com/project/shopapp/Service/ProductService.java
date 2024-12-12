package com.project.shopapp.Service;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImagesDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepo;
import com.project.shopapp.repositories.ProductImageRepo;
import com.project.shopapp.repositories.ProductRepo;
import com.project.shopapp.response.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService implements IProductService{
    ProductRepo productRepo;
    CategoryRepo categoryRepo;
    ProductImageRepo productImageRepo;
    @Override
    public boolean existsByName(String name) {
        return productRepo.existsByName(name);
    }
    @Override
    public List<Product> findProductsByListId(List<Long> listId) {
        return productRepo.findProductsByIds(listId);
    }

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Category category = categoryRepo.findById(productDTO.getCategories_id())
                .orElseThrow(()->new IllegalArgumentException("không tìm thấy category!" + productDTO.getCategories_id()));
        if(!existsByName(productDTO.getName())){
            Product product = Product.builder()
                    .name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .thumbnail(productDTO.getThumbnail())
                    .description(productDTO.getDescription())
                    .category(category)
                    .build();
            productRepo.save(product);
           return product;

        }
        return null;
    }

    @Override
    public Product getProduct(long id) {
        Product product =productRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Not found product"));
        List<ProductImage> productImages=  productImageRepo.findAllByProductId(id);
        product.setProductImages(productImages);
        return product;
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, long categoryId,PageRequest pageRequest) {
        return productRepo.searchProducts(categoryId,keyword,pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) {
        Category category = categoryRepo.findById(productDTO.getCategories_id())
                .orElseThrow(() -> new IllegalArgumentException("không tìm thấy category!"));
        Product product = getProduct(id);
        if (product != null) {
            product = Product.builder()
                    .name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .thumbnail(productDTO.getThumbnail())
                    .description(productDTO.getDescription())
                    .category(category)
                    .build();
            productRepo.save(product);
            return product;
        }
            return null;

    }

    @Override
    public void deleteProduct(long id) {
        productRepo.deleteById(id);
    }
    @Override
    public ProductImage createProductImage(long productId, ProductImagesDTO productImagesDTO){
        Product product = productRepo.findById(productId).orElseThrow(()-> new IllegalArgumentException("Not found product"));
        List<ProductImage> images = productImageRepo.findAllByProductId(product.getId());
        int size = images.size();
        System.out.println("Number of product images: " + size);

        if (size > 5) {
            throw new InvalidParameterException("Number of images <= 5");
        }

        ProductImage productImage = ProductImage.builder()
                .product(product)
                .url_image(productImagesDTO.getUrl_image())
                .build();
      return productImageRepo.save(productImage);
    }
}
