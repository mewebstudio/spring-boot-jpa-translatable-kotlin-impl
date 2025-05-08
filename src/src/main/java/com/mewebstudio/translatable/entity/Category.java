package com.mewebstudio.translatable.entity;

import com.mewebstudio.springboot.jpa.translatable.ITranslatable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category extends AbstractBaseEntity implements ITranslatable<String, CategoryTranslation> {
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("locale ASC")
    private List<CategoryTranslation> translations = new ArrayList<>();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
            "id='" + getId() + '\'' +
            ", createdAt=" + getCreatedAt() +
            ", updatedAt=" + getUpdatedAt() +
            '}';
    }
}
