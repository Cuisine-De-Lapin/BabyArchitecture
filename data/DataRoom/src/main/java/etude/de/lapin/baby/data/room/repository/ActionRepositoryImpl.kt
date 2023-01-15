package etude.de.lapin.baby.data.room.repository

import etude.de.lapin.baby.data.room.dao.ActionDAO
import etude.de.lapin.baby.data.room.mapper.ActionMapper
import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.repository.ActionRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ActionRepositoryImpl @Inject constructor(
    private val actionDAO: ActionDAO,
    private val actionMapper: ActionMapper,
) : ActionRepository {
    override fun getDailyAction(today: Long) = actionDAO.loadDailyAction(today).map {
        it.map { item ->
            actionMapper.mapToAction(item)
        }

    }
    override fun getDailyActionByCategory(today: Long, categoryId: Int) =
        actionDAO.loadDailyActionByCategory(today, categoryId).map {
            it.map { item ->
                actionMapper.mapToAction(item)
            }
        }
    override fun getActionById(id: Int) = actionDAO.loadActionById(id).map { it?.let { actionMapper.mapToAction(it) } }

    override suspend fun deleteByCategoryId(categoryId: Int) = actionDAO.deleteByCategoryId(categoryId)
    override suspend fun insert(action: Action) = actionDAO.insert(actionMapper.mapToEntity(action))
    override suspend fun delete(action: Action) = actionDAO.delete(actionMapper.mapToEntity(action))
}
