package etude.de.lapin.baby.domain.action.repository

import etude.de.lapin.baby.domain.action.model.Action
import kotlinx.coroutines.flow.Flow

interface ActionRepository {
    fun getDailyAction(today: Long): Flow<List<Action>>
    fun getDailyActionByCategory(today: Long, categoryId: Int): Flow<List<Action>>
    fun getActionById(id: Int): Flow<Action?>
    suspend fun deleteByCategoryId(categoryId: Int)
    suspend fun insert(action: Action)
    suspend fun delete(action: Action)
}