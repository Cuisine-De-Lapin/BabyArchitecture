package etude.de.lapin.baby.data.room.dao

import androidx.room.*
import etude.de.lapin.baby.data.room.model.ActionEntity
import etude.de.lapin.baby.data.room.model.ActionResultEntity

@Dao
interface ActionDAO {
    companion object {
        internal const val ONE_DAY = 1000 * 60 * 60 * 24L
    }

    @Query("SELECT * FROM `Action`")
    fun loadAll(): List<ActionEntity>?

    @Query("SELECT a.id, a.categoryId, b.name categoryName, a.volume, a.timestamp, a.memo FROM `Action` a  INNER JOIN `Category` b  ON a.categoryId = b.id")
    fun loadAllAction(): List<ActionResultEntity>?

    @Query("SELECT a.id, a.categoryId, b.name categoryName, a.volume, a.timestamp, a.memo FROM `Action` a  INNER JOIN `Category` b  ON a.categoryId = b.id WHERE timestamp >= :today AND timestamp < (:today + :oneDay)")
    fun loadDailyAction(today: Long, oneDay: Long = ONE_DAY): List<ActionResultEntity>?

    @Query("SELECT a.id, a.categoryId, b.name categoryName, a.volume, a.timestamp, a.memo FROM `Action` a  INNER JOIN `Category` b  ON a.categoryId = b.id WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId")
    fun loadDailyActionByCategory(today: Long, categoryId: Int, oneDay: Long = ONE_DAY): List<ActionResultEntity>?

    @Query("SELECT a.id, a.categoryId, b.name categoryName, a.volume, a.timestamp, a.memo FROM `Action` a  INNER JOIN `Category` b  ON a.categoryId = b.id WHERE a.id = :id")
    fun loadActionById(id: Int): ActionResultEntity?

    @Query("SELECT SUM(volume) FROM `Action` WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId")
    fun calculateTodayVolumeSumByCategory(today: Long, categoryId: Int, oneDay: Long = ONE_DAY): Int

    @Query("SELECT COUNT(*) FROM `Action` WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId")
    fun calculateTodayCountByCategory(today: Long, categoryId: Int, oneDay: Long = ONE_DAY): Int

    @Query("DELETE FROM `Action` WHERE categoryId = :categoryId")
    fun deleteByCategoryId(categoryId: Int)

    // TODO : 카테고리 별로 삭제하는거 추가하기

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(action: ActionEntity)

    @Delete
    fun delete(action: ActionEntity)
}