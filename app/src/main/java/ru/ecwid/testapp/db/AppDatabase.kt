package ru.ecwid.testapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.ecwid.testapp.db.dao.ProductDao
import ru.ecwid.testapp.db.dao.ProductWithWarehouseDao
import ru.ecwid.testapp.db.dao.WarehouseDao
import ru.ecwid.testapp.models.ProductItem
import ru.ecwid.testapp.models.ProductWithWarehouse
import ru.ecwid.testapp.models.WarehouseItem

@Database(
    entities = [
        ProductItem::class,
        WarehouseItem::class,
        ProductWithWarehouse::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun warehouseDao(): WarehouseDao
    abstract fun productWithWarehouseDao(): ProductWithWarehouseDao

    companion object {

        private lateinit var INSTANCE: AppDatabase

        fun get(context: Context): AppDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .createFromAsset("app.db")
                        .build()
                }
            }
            return INSTANCE
        }
    }
}