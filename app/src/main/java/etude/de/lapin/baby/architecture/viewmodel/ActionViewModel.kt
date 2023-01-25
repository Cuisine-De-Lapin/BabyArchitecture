package etude.de.lapin.baby.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import etude.de.lapin.baby.architecture.toTimestamp
import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.usecase.action.ActionGetAllDailyUsecase
import etude.de.lapin.baby.domain.action.usecase.action.ActionInsertUsecase
import etude.de.lapin.baby.domain.action.usecase.category.CategoryGetAllUsecase
import etude.de.lapin.baby.domain.action.usecase.category.CategoryInsertUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ActionViewModel @Inject constructor(
    private val actionGetAllDailyUsecase: ActionGetAllDailyUsecase,
    private val actionInsertUsecase: ActionInsertUsecase,
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
}