package etude.de.lapin.baby.data.room.repository

import etude.de.lapin.baby.data.room.dao.CategoryDAO
import etude.de.lapin.baby.data.room.mapper.CategoryMapper
import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDAO: CategoryDAO,
    private val categoryMapper: CategoryMapper
) : CategoryRepository {
    override suspend fun getAllCategory() = categoryDAO.loadAllCategory()?.map {
        categoryMapper.mapToCategory(it)
    }

    override suspend fun getCategoryById(id: Int) = categoryDAO.loadCategoryById(id)?.let {
        categoryMapper.mapToCategory(it)
    }

    override suspend fun insert(category: Category) =
        categoryDAO.insert(categoryMapper.mapToCategoryEntity(category))

    override suspend fun delete(category: Category) =
        categoryDAO.delete(categoryMapper.mapToCategoryEntity(category))
}
