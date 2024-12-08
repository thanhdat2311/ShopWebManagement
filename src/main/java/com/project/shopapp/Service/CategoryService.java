package com.project.shopapp.Service;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.repositories.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepo categoryRepo;
//    public CategoryService(CategoryRepo categoryRepo){
//        this.categoryRepo = categoryRepo;
//    }
    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category  category= Category.builder().name(categoryDTO.getName()).build();
        return categoryRepo.save(category);
    }

    public Category getCategory(Long id) {
        return  categoryRepo.findById(id).orElse(null);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existCategory = getCategory(id);
        existCategory.setName(categoryDTO.getName());
        categoryRepo.save(existCategory);
        return existCategory;
    }

    @Override
    public void deleteCatgory(Long id) {
    // xóa cứng
        categoryRepo.deleteById(id);
    }
}
