package etude.de.lapin.baby.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import etude.de.lapin.baby.architecture.toTimestamp
import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.usecase.action.ActionDeleteUsecase
import etude.de.lapin.baby.domain.action.usecase.action.ActionGetAllDailyUsecase
import etude.de.lapin.baby.domain.action.usecase.action.ActionInsertUsecase
import etude.de.lapin.baby.domain.action.usecase.action.ActionUpdateUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ActionViewModel @Inject constructor(
    private val actionGetAllDailyUsecase: ActionGetAllDailyUsecase,
    private val actionInsertUsecase: ActionInsertUsecase,
    private val actionUpdateUsecase: ActionUpdateUsecase,
    private val actionDeleteUsecase: ActionDeleteUsecase
): ViewModel() {
    fun getActions(dateTime: LocalDateTime): Flow<List<Action>> = actionGetAllDailyUsecase.invoke(dateTime.toLocalDate().atStartOfDay().toTimestamp())

    fun insertAction(category: Category, timeStamp: Long, memo: String?, volume: Float?) {
        val action = Action(
            categoryId = category.id,
            categoryName = category.name,
            volume = volume,
            timestamp = timeStamp,
            memo = memo
        )
        viewModelScope.launch(Dispatchers.IO) {
            actionInsertUsecase.invoke(action)
        }
    }

    fun updateAction(actionId: Int, category: Category, timeStamp: Long, memo: String?, volume: Float?) {
        val action = Action(
            id = actionId,
            categoryId = category.id,
            categoryName = category.name,
            volume = volume,
            timestamp = timeStamp,
            memo = memo
        )
        viewModelScope.launch(Dispatchers.IO) {
            actionUpdateUsecase.invoke(action)
        }
    }

    fun deleteAction(action: Action) {
        viewModelScope.launch(Dispatchers.IO) {
            actionDeleteUsecase.invoke(action)
        }
    }
}