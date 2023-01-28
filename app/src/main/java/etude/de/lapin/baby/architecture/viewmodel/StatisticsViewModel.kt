package etude.de.lapin.baby.architecture.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import etude.de.lapin.baby.architecture.toTimestamp
import etude.de.lapin.baby.domain.action.model.Statistics
import etude.de.lapin.baby.domain.action.usecase.statistics.StatisticsGetUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsGetUseCase: StatisticsGetUseCase
) : ViewModel() {
    fun getStatistics(dateTime: LocalDateTime): Flow<List<Statistics>> =
        statisticsGetUseCase.invoke(dateTime.toLocalDate().atStartOfDay().toTimestamp()).flowOn(
            Dispatchers.IO
        )
}