package com.project.shopapp.controllers;

import com.github.javafaker.Faker;
import com.project.shopapp.Service.CategoryService;
import com.project.shopapp.Service.ProductService;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImagesDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.response.ProductListResponse;
import com.project.shopapp.response.ProductResponse;
import com.project.shopapp.util.LocalizationUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/products")
@AllArgsConstructor
public class ProductController {
    ProductService productService;
    CategoryService categoryService;
    private final LocalizationUtils localizationUtils;
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProduct(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(value="categoryId", required = false) Long categoryId,
            @RequestParam(value="keyword",required = false) String keyword

            ) {
        PageRequest pageRequest = PageRequest.of(page, limit,
        //        Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );
        Page<ProductResponse> productsPage = productService.getAllProducts(keyword,categoryId,pageRequest);
        int TotalPage = productsPage.getTotalPages();
        List<ProductResponse> productItem = productsPage.getContent();
        ProductListResponse productListResponse = ProductListResponse.builder()
                .products(productItem)
                .TotalPages(TotalPage)
                .build();
        return ResponseEntity.ok(productListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        try {
            Product product = productService.getProduct(productId);
            return ResponseEntity.ok(product);//ProductResponse.fromProduct(product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePrroduct(@PathVariable("id") Long producId) {
        try {
            productService.deleteProduct(producId);
            return ResponseEntity.status(HttpStatus.OK).body("Product delete:" + producId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,

                                           BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> error = result.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(error);
            }
            Product product = productService.createProduct(productDTO);
            return ResponseEntity.ok(product);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") long productId
            , @ModelAttribute("files") List<MultipartFile> files) throws IOException {
        Product product = productService.getProduct(productId);
        files = files == null ? new ArrayList<>() : files;
        List<ProductImage> productImageNew = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getSize() == 0) {
                continue;
            }
            if (file != null) {
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File can not be > 10MB");
                }

                if ((file.getContentType() == null) || !(file.getContentType().startsWith("image/"))) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }

            }
            String name = storeFile(file);
            ProductImage productImage = productService.createProductImage(product.getId(), ProductImagesDTO.builder()
                    .url_image(name)
                    .build());
            productImageNew.add(productImage);
        }
        return ResponseEntity.ok(productImageNew);
    }
    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName){
        try{
            Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource urlResource = new UrlResource(imagePath.toUri());
            if(urlResource.exists()){
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(urlResource);
            }else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
}

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("It is not a file");
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename()); //lấy tên gốc lên và làm sạch
        // thêm UUID cho tên
        String UUIDFileName = UUID.randomUUID().toString() + "_" + filename;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        ;
        // đường dẫn đến file
        Path destination = Paths.get(uploadDir.toString(), UUIDFileName);
        // đưa file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return UUIDFileName;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    @GetMapping("/Ids")
    public ResponseEntity<?> getProductsByListIds(@RequestParam("listIdClient") String listIdClient){
        try {
            List<Long> listId = Arrays.stream(listIdClient.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Product> productList = productService.findProductsByListId(listId);
            return ResponseEntity.status(HttpStatus.OK).body(productList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/generateFakerProduct")
    public ResponseEntity<String> generateFaker() {
        Faker faker = new Faker();
        for (int i = 1; i < 1000; i++) {
            ProductDTO productDTO = ProductDTO.builder()
                    .name(faker.commerce().productName())
                    .description(faker.lorem().sentence())
                    .price(Float.valueOf(faker.commerce().price()))
                    .thumbnail(faker.internet().image())
                    .categories_id(Long.valueOf(faker.number().numberBetween(2, 6)))
                    .build();
            do {
                productDTO.setName(faker.commerce().productName());

            }
            while (productService.existsByName(productDTO.getName()));
            productService.createProduct(productDTO);
        }
        return ResponseEntity.ok("ok");
        // okie
    }
}
