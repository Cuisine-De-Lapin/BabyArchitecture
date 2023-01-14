package etude.de.lapin.baby.data.room.mapper

import etude.de.lapin.baby.data.room.model.ActionEntity
import etude.de.lapin.baby.data.room.model.ActionResultEntity
import etude.de.lapin.baby.domain.action.model.Action
import javax.inject.Inject

class ActionMapper @Inject constructor() {
    fun mapToEntity(action: Action) = ActionEntity(
        id = action.id,
        categoryId = action.categoryId,
        volume = action.volume,
        timestamp = action.timestamp,
        memo = action.memo,
    )

    fun mapToAction(actionResultEntity: ActionResultEntity) = Action(
        id = actionResultEntity.id,
        categoryId = actionResultEntity.categoryId,
        categoryName = actionResultEntity.categoryName,
        volume = actionResultEntity.volume,
        timestamp = actionResultEntity.timestamp,
        memo = actionResultEntity.memo
    )
}