package etude.de.lapin.baby.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Action")
data class ActionEntity (
    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo(name = "categoryId")
    val categoryId: Int,

    @ColumnInfo(name = "volume")
    val volume: Int? = null,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "memo")
    val memo: String? = null
)