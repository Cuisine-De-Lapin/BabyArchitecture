package etude.de.lapin.baby.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Action")
class ActionEntity {
    @PrimaryKey
    val id = 0

    @ColumnInfo(name = "categoryId")
    val categoryId: Int? = null

    @ColumnInfo(name = "volume")
    val volume: Int? = null

    @ColumnInfo(name = "timestamp")
    val timestamp: Long? = null
}