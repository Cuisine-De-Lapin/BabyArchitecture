package etude.de.lapin.baby.data.room.repository

import etude.de.lapin.baby.data.room.dao.ActionDAO
import etude.de.lapin.baby.data.room.mapper.ActionMapper
import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.repository.ActionRepository
import javax.inject.Inject

class ActionRepositoryImpl @Inject constructor(
    private val actionDAO: ActionDAO,
    private val actionMapper: ActionMapper,
) : ActionRepository {
    override suspend fun getDailyAction(today: Long) = actionDAO.loadDailyAction(today)?.map {
        actionMapper.mapToAction(it)
    }
    override suspend fun getDailyActionByCategory(today: Long, categoryId: Int) =
        actionDAO.loadDailyActionByCategory(today, categoryId)?.map {
            actionMapper.mapToAction(it)
        }

    override suspend fun getActionById(id: Int) = actionDAO.loadActionById(id)?.let { actionMapper.mapToAction(it) }
    override suspend fun insert(action: Action) = actionDAO.insert(actionMapper.mapToEntity(action))
    override suspend fun delete(action: Action) = actionDAO.delete(actionMapper.mapToEntity(action))
}
