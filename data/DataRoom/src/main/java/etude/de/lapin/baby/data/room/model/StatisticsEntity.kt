package etude.de.lapin.baby.data.room.model

data class StatisticsEntity(
    val categoryId: Int,
    val categoryName: String,
    val totalCount: Int,
    val totalVolume: Float?,
    val avgVolume: Float?,
    val avgInternalTime: Float
)
