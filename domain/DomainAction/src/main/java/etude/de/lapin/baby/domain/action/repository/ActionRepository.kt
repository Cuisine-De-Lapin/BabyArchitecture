package etude.de.lapin.baby.domain.action.repository

import etude.de.lapin.baby.domain.action.model.Action

interface ActionRepository {
    suspend fun getDailyAction(today: Long): List<Action>?
    suspend fun getDailyActionByCategory(today: Long, categoryId: Int): List<Action>?
    suspend fun getActionById(id: Int): Action?
    suspend fun insert(action: Action)
    suspend fun delete(action: Action)
}