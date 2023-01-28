package etude.de.lapin.baby.domain.action.repository

import etude.de.lapin.baby.domain.action.model.Statistics
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    fun getStatistics(today: Long): Flow<List<Statistics>>
}