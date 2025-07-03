package com.desirArman.blog.controllers;

import com.desirArman.blog.domain.dtos.CategoryDto;
import com.desirArman.blog.domain.dtos.CreateCategoryRequest;
import com.desirArman.blog.domain.entities.Category;
import com.desirArman.blog.mapper.CategoryMapper;
import com.desirArman.blog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategory(){
         List<CategoryDto> categories = categoryService.listCategories()
                 .stream().map(category -> categoryMapper.toDto(category))
                 .toList();

         return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest ){
            Category NewCategory = categoryMapper.toEntity(createCategoryRequest);
            Category savedCategory = categoryService.createCategory(NewCategory);
            return new ResponseEntity<>(
                    categoryMapper.toDto(savedCategory),
                    HttpStatus.CREATED
            );
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
