package ru.ecwid.testapp.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class ProductWarehousesPair {

    @Embedded
    lateinit var product: ProductItem

    @Relation(
        parentColumn = "productId",
        entityColumn = "warehouseId",
        associateBy = Junction(
            ProductWithWarehouse::class
        )
    )

    lateinit var warehouses: List<WarehouseItem>
}