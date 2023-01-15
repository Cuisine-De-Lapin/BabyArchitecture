package etude.de.lapin.baby.domain.action.usecase.action

import etude.de.lapin.baby.domain.action.repository.ActionRepository
import javax.inject.Inject

class ActionGetDailyByCategoryUsecase @Inject constructor(private val actionRepository: ActionRepository) {
    operator fun invoke(today: Long, categoryId: Int) = actionRepository.getDailyActionByCategory(today, categoryId)
}