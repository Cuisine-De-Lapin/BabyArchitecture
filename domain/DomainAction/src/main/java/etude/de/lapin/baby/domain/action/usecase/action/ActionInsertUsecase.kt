package etude.de.lapin.baby.domain.action.usecase.action

import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.repository.ActionRepository
import javax.inject.Inject

class ActionInsertUsecase @Inject constructor(private val actionRepository: ActionRepository) {
    suspend operator fun invoke(action: Action) {
        actionRepository.insert(action)
    }
}