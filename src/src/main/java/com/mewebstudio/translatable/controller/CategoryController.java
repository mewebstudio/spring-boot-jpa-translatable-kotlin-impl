package com.mewebstudio.translatable.controller;

import com.mewebstudio.translatable.dto.request.CreateCategoryRequest;
import com.mewebstudio.translatable.dto.request.UpdateCategoryRequest;
import com.mewebstudio.translatable.dto.response.CategoryResponse;
import com.mewebstudio.translatable.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Retrieves all categories.
     *
     * @return a response entity containing a list of all categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> all() {
        return ResponseEntity.ok(
            categoryService.getAllCategories().stream()
                .map(CategoryResponse::convert)
                .toList()
        );
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category to retrieve
     * @return a response entity containing the category data
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> show(@PathVariable("id") String id) {
        return ResponseEntity.ok(CategoryResponse.convert(categoryService.findById(id)));
    }

    /**
     * Creates a new category.
     *
     * @param request the request body containing category data
     * @return a response entity containing the created category data
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CreateCategoryRequest request) {
        return new ResponseEntity<>(CategoryResponse.convert(categoryService.create(request)), HttpStatus.CREATED);
    }

    /**
     * Updates an existing category.
     *
     * @param id      the ID of the category to update
     * @param request the request body containing updated category data
     * @return a response entity containing the updated category data
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
        @PathVariable String id,
        @RequestBody @Valid UpdateCategoryRequest request
    ) {
        return ResponseEntity.ok(CategoryResponse.convert(categoryService.update(id, request)));
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @return a response entity indicating the result of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
