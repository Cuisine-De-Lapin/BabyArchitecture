package etude.de.lapin.baby.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
class CategoryEntity {
    @PrimaryKey
    val id = 0

    @ColumnInfo(name = "name")
    val name: String? = null

    @ColumnInfo(name = "needVolume")
    val needVolume: Boolean? = null

    // visible == false인 경우, 카테고리는 없애되, 이미 남아있는 아이템은 유지하도록 하기 위함
    @ColumnInfo(name = "visible")
    val visible: Boolean = true
}