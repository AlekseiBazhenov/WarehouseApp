package ru.ecwid.testapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.ecwid.testapp.models.ProductWarehousesPair
import ru.ecwid.testapp.models.ProductWithWarehouse

@Dao
interface ProductWithWarehouseDao {

    @Insert
    suspend fun insertAll(item: List<ProductWithWarehouse>)

    @Query("DELETE FROM product_warehouse WHERE productId = :id")
    suspend fun delete(id: Long)

    @Transaction
    @Query("SELECT * FROM product WHERE productId= :id")
    fun getProductWithWarehouses(id: Long): LiveData<ProductWarehousesPair>

}