package com.mewebstudio.translatable.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CategoryCategoryTranslationRequest(
    @field:NotNull(message = "Name cannot be null")
    @field:NotBlank(message = "Name cannot be blank")
    val name: String?,

    val description: String?
)
