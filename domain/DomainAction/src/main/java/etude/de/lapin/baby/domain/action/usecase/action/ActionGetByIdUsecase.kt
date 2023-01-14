package etude.de.lapin.baby.domain.action.usecase.action

import etude.de.lapin.baby.domain.action.repository.ActionRepository
import javax.inject.Inject

class ActionGetByIdUsecase @Inject constructor(private val actionRepository: ActionRepository) {
    suspend operator fun invoke(id: Int) {
        actionRepository.getActionById(id)
    }
}