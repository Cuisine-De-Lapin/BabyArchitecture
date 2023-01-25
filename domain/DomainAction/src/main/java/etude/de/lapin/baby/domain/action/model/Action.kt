package etude.de.lapin.baby.domain.action.model

data class Action(val id: Int = 0, val categoryId: Int, val categoryName: String, val volume: Float?, val timestamp: Long, val memo: String?)