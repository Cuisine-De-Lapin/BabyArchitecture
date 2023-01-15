package etude.de.lapin.baby.domain.action.repository

import etude.de.lapin.baby.domain.action.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategory(): Flow<List<Category>>
    fun getCategoryById(id: Int): Flow<Category?>
    suspend fun hideCategoryById(id: Int)
    suspend fun insert(category: Category)
    suspend fun delete(category: Category)
}