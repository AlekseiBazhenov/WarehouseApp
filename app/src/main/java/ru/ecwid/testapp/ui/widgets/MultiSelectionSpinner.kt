package ru.ecwid.testapp.ui.widgets

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import ru.ecwid.testapp.R
import ru.ecwid.testapp.models.WarehouseItem
import java.util.*

class MultiSelectionSpinner : AppCompatSpinner, OnMultiChoiceClickListener {

    private lateinit var callback: Callback
    private lateinit var items: List<WarehouseItem>
    private var selection: BooleanArray = booleanArrayOf()
    private var adapter: ArrayAdapter<String>

    constructor(context: Context?) : super(context) {
        adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item)
        super.setAdapter(adapter)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item)
        super.setAdapter(adapter)
    }

    override fun onClick(
        dialog: DialogInterface,
        which: Int,
        isChecked: Boolean
    ) {
        if (which < selection.size) {
            selection[which] = isChecked
            adapter.clear()
            adapter.add(buildSelectedItemString())
        } else {
            throw IllegalArgumentException("Argument 'which' is out of bounds.")
        }
    }

    override fun performClick(): Boolean {
        val builder = AlertDialog.Builder(context)
        val itemNames = arrayOfNulls<String>(items.size)
        for (i in items.indices) {
            itemNames[i] = items[i].address
        }
        builder.setCancelable(false)
        builder.setMultiChoiceItems(itemNames, selection, this)
        builder.setPositiveButton("OK") { _, _ ->
            callback.onOkClick()
        }
        builder.show()
        return true
    }

    override fun setAdapter(adapter: SpinnerAdapter) {
        throw RuntimeException("setAdapter is not supported by MultiSelectSpinner.")
    }

    fun setItems(items: List<WarehouseItem>) {
        this.items = items
        selection = BooleanArray(this.items.size)
        adapter.clear()
        adapter.add("")
        Arrays.fill(selection, false)
        setSelection(arrayListOf())
    }

    fun setSelection(selection: List<WarehouseItem>) {
        for (item in selection) {
            for (j in items.indices) {
                if (items[j].address == item.address) {
                    this.selection[j] = true
                }
            }
        }
        adapter.clear()
        adapter.add(buildSelectedItemString())
    }

    private fun buildSelectedItemString(): String {
        val size = selection.count { b -> b }
        return context.getString(R.string.selected, size)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    val selectedItems: List<WarehouseItem>
        get() {
            val selectedItems = ArrayList<WarehouseItem>()
            for (i in items.indices) {
                if (selection[i]) {
                    selectedItems.add(items[i])
                }
            }
            return selectedItems
        }


    interface Callback {
        fun onOkClick()
    }
}