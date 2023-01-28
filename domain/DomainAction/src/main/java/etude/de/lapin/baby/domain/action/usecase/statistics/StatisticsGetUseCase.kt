package etude.de.lapin.baby.domain.action.usecase.statistics

import etude.de.lapin.baby.domain.action.repository.StatisticsRepository
import javax.inject.Inject

class StatisticsGetUseCase @Inject constructor(private val statisticsRepository: StatisticsRepository) {
    operator fun invoke(today: Long) = statisticsRepository.getStatistics(today)
}