package com.mewebstudio.translatable.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty

data class CreateCategoryRequest(
    @field:NotEmpty(message = "Translations cannot be empty")
    @field:Valid
    val translations: MutableMap<String, CategoryCategoryTranslationRequest> = mutableMapOf()
)
