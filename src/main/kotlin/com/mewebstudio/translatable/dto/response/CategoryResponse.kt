package com.mewebstudio.translatable.dto.response

import java.time.LocalDateTime

data class CategoryResponse(
    val id: String,

    val translations: Map<String, CategoryTranslationResponse>,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
) {
    companion object {
        fun convert(category: com.mewebstudio.translatable.entity.Category): CategoryResponse =
            CategoryResponse(
                id = category.id,
                translations = category.translations
                    .associateBy { it.locale }
                    .mapValues { CategoryTranslationResponse.convert(it.value, false) },
                createdAt = category.createdAt,
                updatedAt = category.updatedAt
            )
    }
}
