package me.maximys777.Video.controller;

import lombok.AllArgsConstructor;
import me.maximys777.Video.dto.request.CategoryRequest;
import me.maximys777.Video.dto.response.CategoryResponse;
import me.maximys777.Video.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SAO/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    //Put
    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse categoryResponse = categoryService.createCategory(request);
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{name}")
    public CategoryResponse updateCategory(@PathVariable String name, @RequestBody CategoryRequest request) {
         return categoryService.updateCategory(request, name);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable String name) {
        CategoryResponse categoryResponse = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategory() {
        List<CategoryResponse> category = categoryService.getAllCategories();
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable String name) {
        categoryService.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }

}
