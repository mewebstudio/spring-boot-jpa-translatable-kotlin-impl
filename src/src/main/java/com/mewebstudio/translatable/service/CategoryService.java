package com.mewebstudio.translatable.service;

import com.mewebstudio.springboot.jpa.translatable.AbstractTranslatableService;
import com.mewebstudio.translatable.dto.request.CreateCategoryRequest;
import com.mewebstudio.translatable.dto.request.UpdateCategoryRequest;
import com.mewebstudio.translatable.entity.Category;
import com.mewebstudio.translatable.entity.CategoryTranslation;
import com.mewebstudio.translatable.exception.BadRequestException;
import com.mewebstudio.translatable.exception.NotFoundException;
import com.mewebstudio.translatable.repository.CategoryRepository;
import com.mewebstudio.translatable.repository.CategoryTranslationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CategoryService extends AbstractTranslatableService<Category, String, CategoryTranslation> {
    private final CategoryRepository categoryRepository;
    private final CategoryTranslationRepository categoryTranslationRepository;

    /**
     * Constructs a new CategoryService.
     *
     * @param categoryRepository            the category repository
     * @param categoryTranslationRepository the category translation repository
     */
    public CategoryService(CategoryRepository categoryRepository, CategoryTranslationRepository categoryTranslationRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
        this.categoryTranslationRepository = categoryTranslationRepository;

        log.debug("CategoryService initialized with repository: {}", categoryRepository);
        Objects.requireNonNull(categoryRepository, "CategoryRepository cannot be null");
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of all categories
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Finds a category by its ID.
     *
     * @param id the ID of the category
     * @return the category with the given ID
     * @throws NotFoundException if no category is found with the given ID
     */
    public Category findById(String id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    /**
     * Creates a new category with translations.
     *
     * @param request the category creation request containing translations
     * @return the created category
     * @throws BadRequestException if a translation with the same name already exists in the given locale
     */
    @Transactional
    public Category create(CreateCategoryRequest request) {
        Category category = new Category();

        request.getTranslations().forEach((locale, req) -> {
            String name = req.getName();
            if (categoryTranslationRepository.existsByLocaleAndName(locale, name)) {
                throw new BadRequestException("Category with name '" + name + "' already exists in locale '" + locale + "'");
            }

            CategoryTranslation translation = new CategoryTranslation();
            translation.setOwner(category);
            translation.setLocale(locale);
            translation.setName(name);
            translation.setDescription(req.getDescription());

            category.getTranslations().add(translation);
        });

        Category saved = categoryRepository.save(category);
        log.info("Created new category {}", saved);
        return saved;
    }

    /**
     * Updates an existing category and its translations.
     *
     * @param id      the ID of the category to update
     * @param request the update request containing updated translations
     * @return the updated category
     * @throws NotFoundException   if the category does not exist
     * @throws BadRequestException if a translation with the same name already exists in the given locale
     */
    @Transactional
    public Category update(String id, UpdateCategoryRequest request) {
        Category category = findById(id);
        Map<String, CategoryTranslation> existingTranslations = new HashMap<>();
        category.getTranslations().forEach(t -> existingTranslations.put(t.getLocale(), t));

        request.getTranslations().forEach((locale, req) -> {
            CategoryTranslation translation = existingTranslations.get(locale);
            String name = req.getName();

            if (translation != null) {
                if (name != null && !name.equals(translation.getName())
                    && categoryTranslationRepository.existsByLocaleAndNameAndIdIsNot(locale, name, translation.getId())) {
                    throw new BadRequestException("Category with name '" + name + "' already exists in locale '" + locale + "'");
                }

                translation.setName(name != null ? name : translation.getName());
                translation.setDescription(req.getDescription() != null ? req.getDescription() : translation.getDescription());
            } else {
                if (name == null || name.isBlank()) {
                    throw new BadRequestException("Name cannot be blank for new locale '" + locale + "'");
                }

                if (categoryTranslationRepository.existsByLocaleAndName(locale, name)) {
                    throw new BadRequestException("Category with name '" + name + "' already exists in locale '" + locale + "'");
                }

                CategoryTranslation newTranslation = new CategoryTranslation();
                newTranslation.setLocale(locale);
                newTranslation.setName(name);
                newTranslation.setDescription(req.getDescription());
                newTranslation.setOwner(category);

                category.getTranslations().add(newTranslation);
            }
        });

        Category saved = categoryRepository.save(category);
        log.info("Updated category {}", saved);
        return saved;
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @throws NotFoundException if the category does not exist
     */
    public void delete(String id) {
        Category category = findById(id);
        categoryRepository.delete(category);
        log.info("Deleted category {}", id);
    }
}
