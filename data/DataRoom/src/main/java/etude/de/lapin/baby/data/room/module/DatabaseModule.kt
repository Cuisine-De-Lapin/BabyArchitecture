package etude.de.lapin.baby.architecture.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import etude.de.lapin.baby.data.room.BabyDatabase
import etude.de.lapin.baby.data.room.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideBabyDatabase(
        @ApplicationContext context: Context
    ): BabyDatabase {
        return Room.databaseBuilder(
            context,
            BabyDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideActionDAO(
        database: BabyDatabase
    ) = database.actionDao()

    @Provides
    fun provideCategoryDAO(
        database: BabyDatabase
    ) = database.categoryDao()

}