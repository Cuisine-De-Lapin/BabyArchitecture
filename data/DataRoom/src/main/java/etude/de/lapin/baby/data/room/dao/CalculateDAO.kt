package etude.de.lapin.baby.data.room.dao

import androidx.room.Dao
import androidx.room.Query
@Dao
interface CalculateDAO {
    @Query("SELECT SUM(volume) FROM `Action` WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId")
    fun calculateTodayVolumeSumByCategory(today: Long, categoryId: Int, oneDay: Long = ActionDAO.ONE_DAY): Int

    @Query("SELECT COUNT(*) FROM `Action` WHERE timestamp >= :today AND timestamp < (:today + :oneDay) AND categoryId = :categoryId")
    fun calculateTodayCountByCategory(today: Long, categoryId: Int, oneDay: Long = ActionDAO.ONE_DAY): Int

}