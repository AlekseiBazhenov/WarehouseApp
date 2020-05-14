package ru.ecwid.testapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.ecwid.testapp.db.repositories.ProductsRepository
import ru.ecwid.testapp.models.ProductItem
import ru.ecwid.testapp.models.ProductWarehousesPair
import ru.ecwid.testapp.models.ProductWithWarehouse
import ru.ecwid.testapp.models.WarehouseItem


class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ProductsRepository =
        ProductsRepository(application)

    private var allProducts: LiveData<List<ProductItem>> = repository.getAllProducts()

    fun insert(
        item: ProductItem,
        selectedWarehouses: List<WarehouseItem>
    ) {
        val id = repository.insert(item)
        insertWarehouses(selectedWarehouses, id)
    }

    fun update(
        item: ProductItem,
        selectedWarehouses: List<WarehouseItem>
    ) {
        repository.updateProduct(item)
        insertWarehouses(selectedWarehouses, item.productId)
    }

    private fun insertWarehouses(
        selectedWarehouses: List<WarehouseItem>,
        itemId: Long
    ) {
        val list = arrayListOf<ProductWithWarehouse>()
        selectedWarehouses.forEach {
            val item = ProductWithWarehouse()
            item.productId = itemId
            item.warehouseId = it.warehouseId
            list.add(item)
        }
        repository.insertWarehouses(itemId, list)
    }

    fun getAllProducts(): LiveData<List<ProductItem>> {
        return allProducts
    }

    fun getProduct(id: Long): LiveData<ProductWarehousesPair> {
        return repository.getProduct(id)
    }

    fun delete(id: Long) {
        repository.delete(id)
    }
}