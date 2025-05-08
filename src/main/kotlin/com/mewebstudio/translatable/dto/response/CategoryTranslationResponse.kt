package com.mewebstudio.translatable.dto.response

import com.mewebstudio.translatable.entity.CategoryTranslation
import java.time.LocalDateTime

data class CategoryTranslationResponse(
    val name: String,

    val description: String?,

    val category: CategoryResponse?,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
) {
    companion object {
        fun convert(translation: CategoryTranslation, includeOwner: Boolean = false): CategoryTranslationResponse =
            CategoryTranslationResponse(
                name = translation.name,
                description = translation.description,
                category = if (includeOwner) {
                    CategoryResponse.convert(translation.owner)
                } else {
                    null
                },
                createdAt = translation.createdAt,
                updatedAt = translation.updatedAt
            )
    }
}
