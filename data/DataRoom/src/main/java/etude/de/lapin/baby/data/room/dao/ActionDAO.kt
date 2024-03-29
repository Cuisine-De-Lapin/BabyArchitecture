package etude.de.lapin.baby.data.room.dao

import androidx.room.*
import etude.de.lapin.baby.data.room.model.ActionEntity
import etude.de.lapin.baby.data.room.model.ActionResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionDAO {
    companion object {
        internal const val ONE_DAY = 1000 * 60 * 60 * 24L
    }

    @Query("SELECT * FROM `Action`")
    fun loadAll(): List<ActionEntity>?

    @Query("SELECT a.id, a.categoryId, b.name categoryName, a.volume, a.timestamp, a.memo FROM `Action` a  INNER JOIN `Category` b  ON a.categoryId = b.id ORDER BY timestamp")
    fun loadAllAction(): Flow<List<ActionResultEntity>>

    @Query("SELECT a.id, a.categoryId, b.name categoryName, a.volume, a.timestamp, a.memo FROM `Action` a  INNER JOIN `Category` b  ON a.categoryId = b.id WHERE timestamp >= :today AND timestamp < (:today + :oneDay) ORDER BY timestamp")
    fun loadDailyAction(today: Long, oneDay: Long = ONE_DAY): Flow<List<ActionResultEntity>>

    @Query("SELECT a.id, a.categoryId, b.name categoryName, a.volume, a.timestamp, a.memo FROM `Action` a  INNER JOIN `Category` b  ON a.categoryId = b.id WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId ORDER BY timestamp")
    fun loadDailyActionFlowByCategory(today: Long, categoryId: Int, oneDay: Long = ONE_DAY): Flow<List<ActionResultEntity>>

    @Query("SELECT a.id, a.categoryId, b.name categoryName, a.volume, a.timestamp, a.memo FROM `Action` a  INNER JOIN `Category` b  ON a.categoryId = b.id WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId ORDER BY timestamp")
    fun loadDailyActionByCategory(today: Long, categoryId: Int, oneDay: Long = ONE_DAY): List<ActionResultEntity>

    @Query("SELECT a.id, a.categoryId, b.name categoryName, a.volume, a.timestamp, a.memo FROM `Action` a  INNER JOIN `Category` b  ON a.categoryId = b.id WHERE a.id = :id")
    fun loadActionById(id: Int): Flow<ActionResultEntity?>

    @Query("SELECT AVG(volume) FROM `Action` WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId")
    fun calculateTodayVolumeAvgByCategory(today: Long, categoryId: Int, oneDay: Long = ONE_DAY): Float?

    @Query("SELECT SUM(volume) FROM `Action` WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId")
    fun calculateTodayVolumeSumByCategory(today: Long, categoryId: Int, oneDay: Long = ONE_DAY): Float?

    @Query("SELECT COUNT(*) FROM `Action` WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId")
    fun calculateTodayCountByCategory(today: Long, categoryId: Int, oneDay: Long = ONE_DAY): Int

    @Query("DELETE FROM `Action` WHERE categoryId = :categoryId")
    fun deleteByCategoryId(categoryId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(action: ActionEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(action: ActionEntity)

    @Delete
    fun delete(action: ActionEntity)
}