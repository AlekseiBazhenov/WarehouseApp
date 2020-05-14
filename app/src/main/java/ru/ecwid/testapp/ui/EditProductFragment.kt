package ru.ecwid.testapp.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_product_details.*
import ru.ecwid.testapp.R
import ru.ecwid.testapp.models.ProductItem
import ru.ecwid.testapp.models.ProductWarehousesPair
import ru.ecwid.testapp.models.WarehouseItem
import java.io.File


class EditProductFragment : AddProductFragment() {

    private lateinit var navController: NavController

    private val args: EditProductFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        delete.visibility = View.VISIBLE

        navController = NavHostFragment.findNavController(this)

        val productId: Long = args.productId

        val productLiveData = productsViewModel.getProduct(productId)
        productLiveData.observe(
            viewLifecycleOwner,
            Observer { item ->
                setupProductInfo(item)
            }
        )

        delete.setOnClickListener {
            productLiveData.removeObservers(viewLifecycleOwner)
            productsViewModel.delete(productId)
            closeScreen()
        }

        save.setOnClickListener {
            val newTitle = title.text.toString()
            val newPrice = price.text.toString().toDouble()

            update(productId, newTitle, newPrice)
            closeScreen()
        }
    }

    private fun setupProductInfo(item: ProductWarehousesPair) {
        title.setText(item.product.title)
        price.setText(item.product.price.toString())

        picturePath = item.product.photo
        if (!picturePath.isBlank()) {
            val imageFile = File(picturePath)
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            image.setImageBitmap(bitmap)
        }

        setupSelectedWarehouses(item.warehouses)
    }

    private fun setupSelectedWarehouses(items: List<WarehouseItem>) {
        warehouseViewModel.getAllWarehouses().observe(
            viewLifecycleOwner,
            Observer {
                selectWarehouses.setItems(it)
                selectWarehouses.setSelection(items)

                setupAddresses()
            }
        )
    }

    override fun openMap(selectedItems: List<WarehouseItem>) {
        val action = EditProductFragmentDirections.actionToMaps(selectedItems.toTypedArray())
        navController.navigate(action)
    }

    private fun update(productId: Long, newTitle: String, newPrice: Double) {
        val updatedItem = ProductItem()
        updatedItem.productId = productId
        updatedItem.title = newTitle
        updatedItem.price = newPrice
        updatedItem.photo = picturePath
        val selected = selectWarehouses.selectedItems
        productsViewModel.update(updatedItem, selected)
    }
}
