package etude.de.lapin.baby.domain.action.repository

import etude.de.lapin.baby.domain.action.model.Category

interface CategoryRepository {
    suspend fun getAllCategory(): List<Category>?
    suspend fun getCategoryById(id: Int): Category?
    suspend fun insert(category: Category)
    suspend fun delete(category: Category)
}