package com.mewebstudio.translatable.dto.response;

import com.mewebstudio.translatable.entity.CategoryTranslation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTranslationResponse {
    private String name;
    private String description;
    private CategoryResponse category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CategoryTranslationResponse convert(CategoryTranslation translation, boolean includeOwner) {
        return CategoryTranslationResponse.builder()
            .name(translation.getName())
            .description(translation.getDescription())
            .category(includeOwner ? CategoryResponse.convert(translation.getOwner()) : null)
            .createdAt(translation.getCreatedAt())
            .updatedAt(translation.getUpdatedAt())
            .build();
    }
}
