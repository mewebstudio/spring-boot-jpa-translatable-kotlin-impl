package com.mewebstudio.translatable.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateCategoryRequest {
    @NotEmpty(message = "Translations cannot be empty")
    @Valid
    private Map<String, CategoryCategoryTranslationRequest> translations = new HashMap<>();
}
