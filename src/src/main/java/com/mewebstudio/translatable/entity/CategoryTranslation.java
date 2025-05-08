package com.mewebstudio.translatable.entity;

import com.mewebstudio.springboot.jpa.translatable.ITranslation;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
    name = "category_translations",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"locale", "name"}, name = "uk_category_translations_locale_name")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTranslation extends AbstractBaseEntity implements ITranslation<String, Category> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category owner;

    @Column(name = "locale", nullable = false)
    private String locale;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
            "id='" + getId() + '\'' +
            ", owner=" + owner +
            ", locale='" + locale + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", createdAt=" + getCreatedAt() +
            ", updatedAt=" + getUpdatedAt() +
            '}';
    }
}
