package com.mewebstudio.translatable.repository;

import com.mewebstudio.springboot.jpa.translatable.JpaTranslationRepository;
import com.mewebstudio.translatable.entity.Category;
import com.mewebstudio.translatable.entity.CategoryTranslation;

public interface CategoryTranslationRepository extends JpaTranslationRepository<CategoryTranslation, String, Category> {
    boolean existsByLocaleAndName(String locale, String name);

    boolean existsByLocaleAndNameAndIdIsNot(String locale, String name, String id);
}
