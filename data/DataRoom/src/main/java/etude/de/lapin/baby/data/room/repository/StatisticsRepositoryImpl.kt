package etude.de.lapin.baby.data.room.repository

import etude.de.lapin.baby.data.room.dao.ActionDAO
import etude.de.lapin.baby.data.room.dao.CategoryDAO
import etude.de.lapin.baby.data.room.mapper.ActionMapper
import etude.de.lapin.baby.data.room.model.StatisticsEntity
import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.repository.ActionRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val actionDAO: ActionDAO,
    private val categoryDAO: CategoryDAO,
) {
    fun getStatistics(today: Long) {
        categoryDAO.loadAllCategory().map {
            it.map { category ->
                StatisticsEntity(
                    categoryId = category.id,
                    categoryName = category.name,
                    totalCount = actionDAO.calculateTodayCountByCategory(today, category.id),
                    totalVolume = actionDAO.calculateTodayVolumeSumByCategory(today, category.id),
                    avgVolume = actionDAO.calculateTodayVolumeAvgByCategory(today, category.id),
                    avgInternalTime = getAvgInternalTime(today, category.id)
                )
            }

        }
    }

    private fun getAvgInternalTime(today: Long, categoryId: Int): Float {
        var totalTime: Long = 0L
        var lastTime: Long? = null
        val actionList = actionDAO.loadDailyActionByCategory(today, categoryId)
        actionList.forEach { action ->
            lastTime?.let { time -> totalTime = totalTime.plus(action.timestamp.minus(time)) }
            lastTime = action.timestamp
        }

        return totalTime.div(actionList.size.minus(1).toFloat())
    }
}
