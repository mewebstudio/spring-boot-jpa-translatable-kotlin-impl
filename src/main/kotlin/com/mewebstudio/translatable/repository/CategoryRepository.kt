package com.mewebstudio.translatable.repository

import com.mewebstudio.springboot.jpa.translatable.kotlin.JpaTranslatableRepository
import com.mewebstudio.translatable.entity.Category
import com.mewebstudio.translatable.entity.CategoryTranslation

interface CategoryRepository : JpaTranslatableRepository<Category, String, CategoryTranslation>
