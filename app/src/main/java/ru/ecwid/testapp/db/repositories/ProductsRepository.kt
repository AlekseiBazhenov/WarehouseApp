package ru.ecwid.testapp.db.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.ecwid.testapp.db.AppDatabase
import ru.ecwid.testapp.models.ProductItem
import ru.ecwid.testapp.models.ProductWarehousesPair
import ru.ecwid.testapp.models.ProductWithWarehouse
import kotlin.coroutines.CoroutineContext

class ProductsRepository(private val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    fun getAllProducts(): LiveData<List<ProductItem>> =
        AppDatabase.get(context).productDao().getAllProducts()

    fun getProduct(id: Long): LiveData<ProductWarehousesPair> =
        AppDatabase.get(context).productWithWarehouseDao().getProductWithWarehouses(id)

    fun insertWarehouses(
        productId: Long,
        item: List<ProductWithWarehouse>
    ) {
        launch {
            AppDatabase.get(context).productWithWarehouseDao().delete(productId)
            AppDatabase.get(context).productWithWarehouseDao().insertAll(item)
        }
    }

    fun insert(item: ProductItem): Long {
        var id: Long = 0
        runBlocking {
            id = AppDatabase.get(context).productDao().insert(item)
        }
        return id
    }

    fun updateProduct(item: ProductItem) {
        launch {
            AppDatabase.get(context).productDao().update(item)
        }
    }

    fun delete(id: Long) {
        launch {
            AppDatabase.get(context).productDao().delete(id)
        }
    }
}