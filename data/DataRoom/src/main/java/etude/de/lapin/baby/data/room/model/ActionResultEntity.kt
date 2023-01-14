package etude.de.lapin.baby.data.room.model

data class ActionResultEntity (
    val id: Int = 0,
    val categoryId: Int,
    val categoryName: String,
    val volume: Int? = null,
    val timestamp: Long,
    val memo: String? = null

)