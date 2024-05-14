package com.base.projectbase.controller;

import com.base.projectbase.model.Category;
import com.base.projectbase.repository.CategoryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping
    public List<Category> findAll(){
        return categoryRepo.findAll();
    }

    @PostMapping
    public Category create(@RequestBody Category category){
        return categoryRepo.save(category);
    }

    @GetMapping("/{id}")
    public Category get(@PathVariable ObjectId id){
        return categoryRepo.findCategoryById(id);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable ObjectId id, @RequestBody Category category){
        Category oldCategory = categoryRepo.findById(id.toHexString())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        oldCategory.setName(category.getName()); // Set the name from the request body
        return categoryRepo.save(oldCategory);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable ObjectId id){
        categoryRepo.deleteById(id.toHexString());
    }
}