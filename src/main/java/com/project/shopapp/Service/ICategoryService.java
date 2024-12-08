package com.project.shopapp.Service;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;

import java.util.List;

public interface ICategoryService
{
    Category createCategory(CategoryDTO category);
    Category getCategory(Long id);
    List<Category> getAllCategory();
    Category updateCategory(Long id, CategoryDTO category);
    void deleteCatgory(Long id);
}
