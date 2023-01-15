package etude.de.lapin.baby.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import etude.de.lapin.baby.data.room.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {
    @Query("SELECT * FROM `Category` WHERE visible IS NOT 0")
    fun loadAllCategory(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM `Category` WHERE id = :id")
    fun loadCategoryById(id: Int): Flow<CategoryEntity?>

    @Query("UPDATE `Category` SET visible = 0 WHERE id = :id")
    fun hideCategoryById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(action: CategoryEntity)

    @Delete
    fun delete(action: CategoryEntity)
}