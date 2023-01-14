package etude.de.lapin.baby.domain.action.model

data class Action(val id: Int, val categoryId: Int, val categoryName: String, val volume: Int?, val timestamp: Long, val memo: String?)