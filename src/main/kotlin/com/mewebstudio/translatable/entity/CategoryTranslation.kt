package com.mewebstudio.translatable.entity

import com.mewebstudio.springboot.jpa.translatable.kotlin.ITranslation
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(
    name = "category_translations",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["locale", "name"], name = "uk_category_translations_locale_name")
    ]
)
class CategoryTranslation(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    override val owner: Category,

    @Column(name = "locale", nullable = false)
    override val locale: String,

    @Column(name = "name", nullable = false, length = 255)
    var name: String,

    @Column(name = "description", columnDefinition = "text")
    var description: String? = null,
) : AbstractBaseEntity(), ITranslation<String, Category> {
    override fun toString(): String =
        "${this::class.simpleName}(id = $id, name = $name, locale = $locale, owner = $owner)"
}
