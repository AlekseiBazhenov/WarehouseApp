package ru.ecwid.testapp.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warehouse")
class WarehouseItem() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var warehouseId: Long = 0
    var address: String = ""
    var lat = 0.0
    var lon = 0.0


    constructor(parcel: Parcel) : this() {
        warehouseId = parcel.readLong()
        address = parcel.readString()!!
        lat = parcel.readDouble()
        lon = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(warehouseId)
        parcel.writeString(address)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WarehouseItem> {
        override fun createFromParcel(parcel: Parcel): WarehouseItem {
            return WarehouseItem(parcel)
        }

        override fun newArray(size: Int): Array<WarehouseItem?> {
            return arrayOfNulls(size)
        }
    }

}