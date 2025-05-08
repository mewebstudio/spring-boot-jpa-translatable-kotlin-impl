package com.mewebstudio.translatable.controller

import com.mewebstudio.translatable.dto.request.CreateCategoryRequest
import com.mewebstudio.translatable.dto.request.UpdateCategoryRequest
import com.mewebstudio.translatable.dto.response.CategoryResponse
import com.mewebstudio.translatable.service.CategoryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController(private val categoryService: CategoryService) {
    /**
     * Retrieves all categories.
     *
     * @return a response entity containing a list of all categories
     */
    @GetMapping
    fun all(): ResponseEntity<List<CategoryResponse>> =
        ResponseEntity.ok(categoryService.getAllCategories().map { CategoryResponse.convert(it) })

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category to retrieve
     * @return a response entity containing the category data
     */
    @GetMapping("/{id}")
    fun show(@PathVariable("id") id: String): ResponseEntity<CategoryResponse> =
        ResponseEntity.ok(CategoryResponse.convert(categoryService.findById(id)))

    /**
     * Creates a new category.
     *
     * @param request the request body containing category data
     * @return a response entity containing the created category data
     */
    @PostMapping
    fun create(@RequestBody @Valid request: CreateCategoryRequest): ResponseEntity<CategoryResponse> =
        ResponseEntity(CategoryResponse.convert(categoryService.create(request)), HttpStatus.CREATED)

    /**
     * Updates an existing category.
     *
     * @param id      the ID of the category to update
     * @param request the request body containing updated category data
     * @return a response entity containing the updated category data
     */
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody @Valid request: UpdateCategoryRequest
    ): ResponseEntity<CategoryResponse> =
        ResponseEntity.ok(CategoryResponse.convert(categoryService.update(id, request)))

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @return a response entity indicating the result of the deletion
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> = run {
        categoryService.delete(id)
        ResponseEntity.noContent().build()
    }
}
