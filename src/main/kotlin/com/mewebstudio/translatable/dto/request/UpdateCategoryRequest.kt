package com.mewebstudio.translatable.dto.request

import jakarta.validation.Valid

data class UpdateCategoryRequest(
    @field:Valid
    val translations: MutableMap<String, CategoryCategoryTranslationRequest> = mutableMapOf()
)
