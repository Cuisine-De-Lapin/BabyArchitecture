package etude.de.lapin.baby.data.room.repository

import etude.de.lapin.baby.data.room.dao.ActionDAO
import etude.de.lapin.baby.data.room.dao.CategoryDAO
import etude.de.lapin.baby.data.room.mapper.ActionMapper
import etude.de.lapin.baby.data.room.mapper.StatisticsMapper
import etude.de.lapin.baby.data.room.model.StatisticsEntity
import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.model.Statistics
import etude.de.lapin.baby.domain.action.repository.ActionRepository
import etude.de.lapin.baby.domain.action.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val actionDAO: ActionDAO,
    private val categoryDAO: CategoryDAO,
    private val statisticsMapper: StatisticsMapper
): StatisticsRepository {
    override fun getStatistics(today: Long): Flow<List<Statistics>> {
        return categoryDAO.loadAllCategory().map {
            it.map { category ->
                val entity = StatisticsEntity(
                    categoryId = category.id,
                    categoryName = category.name,
                    totalCount = actionDAO.calculateTodayCountByCategory(today, category.id),
                    totalVolume = actionDAO.calculateTodayVolumeSumByCategory(today, category.id),
                    avgVolume = actionDAO.calculateTodayVolumeAvgByCategory(today, category.id),
                    avgInternalTime = getAvgInternalTime(today, category.id)
                )

                statisticsMapper.mapToStatistics(entity)
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

        return actionList.size.minus(1).takeIf { it > 0 }?.let {
            totalTime.div(it).toFloat()
        } ?: 0f
    }
}
