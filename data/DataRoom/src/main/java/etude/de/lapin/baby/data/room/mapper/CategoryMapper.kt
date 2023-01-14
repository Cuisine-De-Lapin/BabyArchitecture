package etude.de.lapin.baby.data.room.mapper

import etude.de.lapin.baby.data.room.model.CategoryEntity
import etude.de.lapin.baby.domain.action.model.Category
import javax.inject.Inject

class CategoryMapper @Inject constructor() {
    fun mapToCategory(categoryEntity: CategoryEntity) = Category(
        id = categoryEntity.id,
        name = categoryEntity.name,
        needVolume = categoryEntity.needVolume,
        visible = categoryEntity.visible
    )

    fun mapToCategoryEntity(category: Category) = CategoryEntity(
        id = category.id,
        name = category.name,
        needVolume = category.needVolume,
        visible = category.visible
    )
}