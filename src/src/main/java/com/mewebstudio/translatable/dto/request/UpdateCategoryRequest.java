package com.mewebstudio.translatable.dto.request;

import jakarta.validation.Valid;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCategoryRequest {
    @Valid
    private Map<String, CategoryCategoryTranslationRequest> translations = new HashMap<>();
}
