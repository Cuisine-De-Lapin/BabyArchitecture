package etude.de.lapin.baby.domain.action.model

data class Statistics(
    val categoryId: Int,
    val categoryName: String,
    val totalCount: Int,
    val totalVolume: Float?,
    val avgVolume: Float?,
    val avgInternalTime: Float
)
