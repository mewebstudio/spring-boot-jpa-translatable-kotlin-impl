package com.mewebstudio.translatable.entity

import com.mewebstudio.springboot.jpa.translatable.kotlin.ITranslatable
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table

@Entity
@Table(name = "categories")
class Category(
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("locale ASC")
    override var translations: MutableList<CategoryTranslation> = mutableListOf(),
) : AbstractBaseEntity(), ITranslatable<String, CategoryTranslation> {
    override fun toString(): String = "${this::class.simpleName}(id = $id)"
}
