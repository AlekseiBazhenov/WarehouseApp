package ru.ecwid.testapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.ecwid.testapp.models.ProductItem

@Dao
interface ProductDao {

    @Insert
    suspend fun insert(item: ProductItem): Long

    @Update
    suspend fun update(item: ProductItem)

    @Query("SELECT * from product ORDER BY productId ASC")
    fun getAllProducts(): LiveData<List<ProductItem>>

    @Query("SELECT * from product WHERE productId= :id")
    fun getProduct(id: Long): LiveData<ProductItem>

    @Query("DELETE FROM product WHERE productId = :id")
    suspend fun delete(id: Long)
}