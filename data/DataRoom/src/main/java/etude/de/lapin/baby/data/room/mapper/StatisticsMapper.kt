package etude.de.lapin.baby.data.room.mapper

import etude.de.lapin.baby.data.room.model.CategoryEntity
import etude.de.lapin.baby.data.room.model.StatisticsEntity
import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.model.Statistics
import javax.inject.Inject

class StatisticsMapper @Inject constructor() {
    fun mapToStatistics(entity: StatisticsEntity) = Statistics(
        categoryId = entity.categoryId,
        categoryName = entity.categoryName,
        totalCount = entity.totalCount,
        totalVolume = entity.totalVolume,
        avgVolume = entity.avgVolume,
        avgInternalTime = entity.avgInternalTime
    )

    fun mapToCategoryEntity(statistics: Statistics) = StatisticsEntity(
        categoryId = statistics.categoryId,
        categoryName = statistics.categoryName,
        totalCount = statistics.totalCount,
        totalVolume = statistics.totalVolume,
        avgVolume = statistics.avgVolume,
        avgInternalTime = statistics.avgInternalTime
    )
}