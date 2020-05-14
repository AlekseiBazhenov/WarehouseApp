package ru.ecwid.testapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.ecwid.testapp.models.WarehouseItem

@Dao
interface WarehouseDao {

    @Insert
    suspend fun insert(item: WarehouseItem)

    @Query("SELECT * from warehouse ORDER BY warehouseId ASC")
    fun getAllWarehouses(): LiveData<List<WarehouseItem>>

}