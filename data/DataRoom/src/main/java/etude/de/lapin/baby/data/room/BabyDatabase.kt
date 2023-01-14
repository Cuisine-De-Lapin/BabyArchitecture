package etude.de.lapin.baby.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import etude.de.lapin.baby.data.room.dao.ActionDAO
import etude.de.lapin.baby.data.room.dao.CategoryDAO
import etude.de.lapin.baby.data.room.model.ActionEntity
import etude.de.lapin.baby.data.room.model.CategoryEntity

@Database(entities = [ActionEntity::class, CategoryEntity::class], version = 1)
abstract class BabyDatabase : RoomDatabase() {
    abstract fun actionDao(): ActionDAO
    abstract fun categoryDao(): CategoryDAO
}