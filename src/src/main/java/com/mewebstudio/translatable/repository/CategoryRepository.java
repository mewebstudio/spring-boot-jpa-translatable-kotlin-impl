package com.mewebstudio.translatable.repository;

import com.mewebstudio.springboot.jpa.translatable.JpaTranslatableRepository;
import com.mewebstudio.translatable.entity.Category;
import com.mewebstudio.translatable.entity.CategoryTranslation;

public interface CategoryRepository extends JpaTranslatableRepository<Category, String, CategoryTranslation> {
}
