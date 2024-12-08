package com.project.shopapp.controllers;

import com.project.shopapp.Service.CategoryService;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.response.UpdateCategoriesResponse;
import com.project.shopapp.util.LocalizationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("api/v1/categories")
//@Validated
//Dependency injection
@AllArgsConstructor
public class CategoryController {
    CategoryService categoryService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    //nếu tham số truyền vào là 1 đối tượng? => object
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                            BindingResult result) {
        if (result.hasErrors()) {
            List<String> Error =  result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(Error);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("this is insert category" + categoryDTO);
    }
    // hiển thị toàn bộ categories
    @GetMapping("") //http://localhost:8088/api/v1/categories?page=1&limit=10
    public ResponseEntity<?> getAllCategories(
         //   @RequestParam("page") int page,
           // @RequestParam("limit") int limit
    ) {
        List<Category> listCategories = categoryService.getAllCategory();
        return ResponseEntity.ok(listCategories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                                 @Valid @RequestBody CategoryDTO categoryDTO,
                                                 HttpServletRequest httpServletRequest
                                                 ) {
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok(UpdateCategoriesResponse.builder()
                .message(localizationUtils.getLocalizationMessages("categories.update.update_successfully"))
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCatgory(id);
        return ResponseEntity.ok("this is delete category " + id);
    }
}
