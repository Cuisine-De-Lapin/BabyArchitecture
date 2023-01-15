package etude.de.lapin.baby.domain.action.usecase.action

import etude.de.lapin.baby.domain.action.repository.ActionRepository
import javax.inject.Inject

class ActionGetAllDailyUsecase @Inject constructor(private val actionRepository: ActionRepository) {
    suspend operator fun invoke(today: Long) = actionRepository.getDailyAction(today)
}