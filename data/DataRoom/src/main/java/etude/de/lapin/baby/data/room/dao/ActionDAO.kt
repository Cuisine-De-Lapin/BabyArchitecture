package etude.de.lapin.baby.data.room.dao

import androidx.room.*
import etude.de.lapin.baby.data.room.model.ActionEntity

@Dao
interface ActionDAO {
    companion object {
        internal const val ONE_DAY = 1000 * 60 * 60 * 24L
    }

    @Query("SELECT * FROM `Action`")
    fun loadAllAction(): List<ActionEntity>?

    @Query("SELECT * FROM `Action` WHERE timestamp >= :today AND timestamp < (:today + :oneDay)")
    fun loadDailyAction(today: Long, oneDay: Long = ONE_DAY): List<ActionEntity>?

    @Query("SELECT * FROM `Action` WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId")
    fun loadDailyActionByCategory(today: Long, categoryId: Int, oneDay: Long = ONE_DAY): List<ActionEntity>?

    @Query("SELECT * FROM `Action` WHERE id = :id")
    fun loadActionById(id: Int): ActionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inset(action: ActionEntity)

    @Delete
    fun delete(action: ActionEntity)
}