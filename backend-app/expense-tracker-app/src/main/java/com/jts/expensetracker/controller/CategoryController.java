package com.jts.expensetracker.controller;

import com.jts.expensetracker.model.Category;
import com.jts.expensetracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("/main")
    public List<String> getMainCategories() {
        return categoryRepository.findAll().stream()
                .map(Category::getType)
                .distinct()
                .toList();
    }

    @GetMapping("/sub/{main}")
    public List<Category> getSubCategories(@PathVariable String main) {
        return categoryRepository.findByType(main);
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }
}
