package ru.ecwid.testapp.db.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ecwid.testapp.db.AppDatabase
import ru.ecwid.testapp.models.WarehouseItem
import kotlin.coroutines.CoroutineContext

class WarehouseRepository(private val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    fun getAllWarehouses(): LiveData<List<WarehouseItem>> =
        AppDatabase.get(context).warehouseDao().getAllWarehouses()

    fun insert(item: WarehouseItem) {
        launch {
            AppDatabase.get(context).warehouseDao().insert(item)
        }
    }
}