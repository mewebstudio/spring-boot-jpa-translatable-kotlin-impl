package com.mewebstudio.translatable.dto.response;

import com.mewebstudio.translatable.entity.Category;
import com.mewebstudio.translatable.entity.CategoryTranslation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String id;
    private Map<String, CategoryTranslationResponse> translations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CategoryResponse convert(Category category) {
        Map<String, CategoryTranslationResponse> translationsMap = category.getTranslations().stream()
            .collect(Collectors.toMap(
                CategoryTranslation::getLocale,
                t -> CategoryTranslationResponse.convert(t, false)
            ));

        return CategoryResponse.builder()
            .id(category.getId())
            .translations(translationsMap)
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .build();
    }
}
