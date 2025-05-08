package com.mewebstudio.translatable.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCategoryTranslationRequest {
    private String name;

    private String description;
}
