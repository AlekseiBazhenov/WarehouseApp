package ru.ecwid.testapp.models

import androidx.room.Entity

// TODO: index
@Entity(tableName = "product_warehouse", primaryKeys = ["productId", "warehouseId"])
class ProductWithWarehouse {
    var productId: Long = 0
    var warehouseId: Long = 0
}