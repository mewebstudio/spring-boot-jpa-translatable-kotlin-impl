package com.mewebstudio.translatable.repository

import com.mewebstudio.springboot.jpa.translatable.kotlin.JpaTranslationRepository
import com.mewebstudio.translatable.entity.Category
import com.mewebstudio.translatable.entity.CategoryTranslation

interface CategoryTranslationRepository : JpaTranslationRepository<CategoryTranslation, String, Category> {
    fun existsByLocaleAndName(locale: String, name: String): Boolean

    fun existsByLocaleAndNameAndIdIsNot(locale: String, name: String, id: String): Boolean
}
