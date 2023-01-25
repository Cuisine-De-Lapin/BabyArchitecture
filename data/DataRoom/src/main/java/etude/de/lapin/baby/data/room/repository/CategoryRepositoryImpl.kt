package etude.de.lapin.baby.data.room.repository

import etude.de.lapin.baby.data.room.dao.CategoryDAO
import etude.de.lapin.baby.data.room.mapper.CategoryMapper
import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.repository.CategoryRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDAO: CategoryDAO,
    private val categoryMapper: CategoryMapper
) : CategoryRepository {
    override fun getAllCategory() = categoryDAO.loadAllCategory().map {
        it.map { item ->
            categoryMapper.mapToCategory(item)
        }
    }

    override fun getCategoryById(id: Int) = categoryDAO.loadCategoryById(id).map {
        it?.let { categoryMapper.mapToCategory(it) }
    }

    override suspend fun hideCategoryById(id: Int) = categoryDAO.hideCategoryById(id)

    override suspend fun insert(category: Category) =
        categoryDAO.insert(categoryMapper.mapToCategoryEntity(category))

    override suspend fun update(category: Category) =
        categoryDAO.update(categoryMapper.mapToCategoryEntity(category))

    override suspend fun delete(category: Category) =
        categoryDAO.delete(categoryMapper.mapToCategoryEntity(category))
}
