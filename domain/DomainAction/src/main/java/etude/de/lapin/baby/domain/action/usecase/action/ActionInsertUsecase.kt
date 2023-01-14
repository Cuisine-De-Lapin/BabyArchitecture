package etude.de.lapin.baby.domain.action.usecase.action

import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.repository.ActionRepository
import etude.de.lapin.baby.domain.action.repository.CategoryRepository
import javax.inject.Inject

class ActionDeleteUsecase @Inject constructor(private val actionRepository: ActionRepository) {
    suspend operator fun invoke(action: Action) {
        actionRepository.delete(action)
    }
}