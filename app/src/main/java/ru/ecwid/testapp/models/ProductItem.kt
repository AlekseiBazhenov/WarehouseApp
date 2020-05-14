package ru.ecwid.testapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO: картинки товара в отдельную таблицу (1:N)

@Entity(tableName = "product")
class ProductItem {

    @PrimaryKey(autoGenerate = true)
    var productId: Long = 0
    var title: String = ""
    var price: Double = 0.0
    var photo: String = ""

}