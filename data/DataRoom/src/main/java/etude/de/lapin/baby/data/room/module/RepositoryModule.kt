package etude.de.lapin.baby.data.room.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import etude.de.lapin.baby.data.room.repository.ActionRepositoryImpl
import etude.de.lapin.baby.data.room.repository.CategoryRepositoryImpl
import etude.de.lapin.baby.data.room.repository.StatisticsRepositoryImpl
import etude.de.lapin.baby.domain.action.repository.ActionRepository
import etude.de.lapin.baby.domain.action.repository.CategoryRepository
import etude.de.lapin.baby.domain.action.repository.StatisticsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindActionRepository(
        actionRepositoryImpl: ActionRepositoryImpl
    ): ActionRepository

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Singleton
    @Binds
    abstract fun bindStatisticsRepository(
        statisticsRepositoryImpl: StatisticsRepositoryImpl
    ): StatisticsRepository
}