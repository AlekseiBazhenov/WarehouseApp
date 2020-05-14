package ru.ecwid.testapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.ecwid.testapp.db.repositories.WarehouseRepository
import ru.ecwid.testapp.models.WarehouseItem


class WarehouseViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: WarehouseRepository =
        WarehouseRepository(application)

    private var allWarehouses: LiveData<List<WarehouseItem>> = repository.getAllWarehouses()

    fun getAllWarehouses(): LiveData<List<WarehouseItem>> {
        return allWarehouses
    }

    fun insert(item: WarehouseItem) {
        repository.insert(item)
    }
}