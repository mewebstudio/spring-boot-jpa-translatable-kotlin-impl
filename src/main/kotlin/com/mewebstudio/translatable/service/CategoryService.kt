package com.mewebstudio.translatable.service

import com.mewebstudio.springboot.jpa.translatable.kotlin.AbstractTranslatableService
import com.mewebstudio.translatable.dto.request.CreateCategoryRequest
import com.mewebstudio.translatable.dto.request.UpdateCategoryRequest
import com.mewebstudio.translatable.entity.Category
import com.mewebstudio.translatable.entity.CategoryTranslation
import com.mewebstudio.translatable.exception.BadRequestException
import com.mewebstudio.translatable.exception.NotFoundException
import com.mewebstudio.translatable.repository.CategoryRepository
import com.mewebstudio.translatable.repository.CategoryTranslationRepository
import com.mewebstudio.translatable.util.logger
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val categoryTranslationRepository: CategoryTranslationRepository
) : AbstractTranslatableService<Category, String, CategoryTranslation>(categoryRepository) {
    private val log: Logger by logger()

    init {
        log.debug("CategoryService initialized with repository: {}", repository)
        requireNotNull(repository) { "CategoryRepository cannot be null" }
    }

    /**
     * Retrieve all categories.
     *
     * @return The list of all categories.
     */
    fun getAllCategories(): List<Category> = categoryRepository.findAll()

    /**
     * Retrieve a category by ID.
     *
     * @param id The ID of the category.
     * @return The category with the specified ID.
     * @throws NotFoundException if the category is not found.
     */
    fun findById(id: String): Category =
        categoryRepository.findById(id).orElseThrow { NotFoundException("Category not found") }

    /**
     * Create a new category.
     *
     * @param request CreateCategoryRequest The request containing the category details.
     * @return Category The created category.
     * @throws BadRequestException if a category with the same name already exists in the specified locale.
     */
    @Transactional
    fun create(request: CreateCategoryRequest): Category = run {
        val category = Category()

        request.translations.forEach { (locale, req) ->
            if (categoryTranslationRepository.existsByLocaleAndName(locale, req.name!!)) {
                throw BadRequestException("Category with name '${req.name}' already exists in locale '$locale'")
            }

            val translation = CategoryTranslation(
                owner = category,
                locale = locale,
                name = req.name,
                description = req.description,
            )
            category.translations.add(translation)
        }

        categoryRepository.save(category).also { log.info("Created new category {}", it) }
    }

    /**
     * Update an existing category.
     *
     * @param id The ID of the category to update.
     * @param request UpdateCategoryRequest The request containing the updated category details.
     * @return Category The updated category.
     * @throws NotFoundException if the category is not found.
     * @throws BadRequestException if a category with the same name already exists in the specified locale.
     */
    @Transactional
    fun update(id: String, request: UpdateCategoryRequest): Category = run {
        val category = findById(id)
        val existingTranslations = category.translations.associateBy { it.locale }.toMutableMap()

        request.translations.forEach { (locale, req) ->
            val translation = existingTranslations[locale]
            if (translation != null) {
                if (req.name != null && req.name != translation.name &&
                    categoryTranslationRepository.existsByLocaleAndNameAndIdIsNot(locale, req.name, translation.id)
                ) {
                    throw BadRequestException("Category with name '${req.name}' already exists in locale '$locale'")
                }

                translation.name = req.name ?: translation.name
                translation.description = req.description ?: translation.description
            } else {
                val name = req.name?.takeIf { it.isNotBlank() }
                    ?: throw BadRequestException("Name cannot be blank for new locale '$locale'")
                if (categoryTranslationRepository.existsByLocaleAndName(locale, req.name)) {
                    throw BadRequestException("Category with name '${req.name}' already exists in locale '$locale'")
                }

                val newTranslation = CategoryTranslation(
                    locale = locale,
                    description = req.description,
                    name = name,
                    owner = category,
                )
                category.translations.add(newTranslation)
            }
        }

        categoryRepository.save(category).also { log.info("Updated category {}", it) }
    }

    /**
     * Delete a category by ID.
     *
     * @param id The ID of the category to delete.
     * @throws NotFoundException if the category is not found.
     */
    fun delete(id: String) = categoryRepository.delete(findById(id)).also { log.info("Deleted category {}", id) }
}
