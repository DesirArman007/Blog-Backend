package com.desirArman.blog.services;

import com.desirArman.blog.domain.entities.Category;

import java.util.List;
import java.util.UUID;


public interface CategoryService {

    public List<Category> listCategories();

    Category createCategory(Category category);

    public void deleteCategory(UUID id);

    Category getCategoryById(UUID id);
}
