package ru.ecwid.testapp.ui

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_product_details.*
import ru.ecwid.testapp.R
import ru.ecwid.testapp.models.ProductItem
import ru.ecwid.testapp.models.WarehouseItem
import ru.ecwid.testapp.ui.viewmodels.ProductViewModel
import ru.ecwid.testapp.ui.viewmodels.WarehouseViewModel
import ru.ecwid.testapp.ui.widgets.MultiSelectionSpinner
import java.io.File


open class AddProductFragment : Fragment() {

    private lateinit var navController: NavController

    protected val productsViewModel by viewModels<ProductViewModel>()
    protected val warehouseViewModel by viewModels<WarehouseViewModel>()

    protected var picturePath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = NavHostFragment.findNavController(this)

        warehouses.visibility = GONE
        selectWarehouses.setCallback(object : MultiSelectionSpinner.Callback {
            override fun onOkClick() {
                setupAddresses()
            }
        })

        warehouseViewModel.getAllWarehouses().observe(
            viewLifecycleOwner,
            Observer {
                selectWarehouses.setItems(it)
            }
        )

        save.setOnClickListener {
            val newTitle = title.text.toString()
            val priceString = price.text.toString()
            val newPrice = if (priceString.isEmpty()) {
                0.0
            } else {
                priceString.toDouble()
            }

            save(newTitle, newPrice)
            closeScreen()
        }

        image.setOnClickListener {
            onSelectImageClick()
        }
    }

    private fun onSelectImageClick() {
        if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            pickImage()
        } else {
            requestPermissionsSafely(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                permissionRequestCode
            )
        }
    }

    protected fun setupAddresses() {
        val selectedItems = selectWarehouses.selectedItems
        if (selectedItems.isEmpty()) {
            warehouses.visibility = GONE
            return
        }

        warehouses.visibility = VISIBLE
        var text = ""
        selectedItems.forEach {
            text = text.plus(it.address + "\n")
        }
        warehouses.text = text.substring(0, text.length - 1)
        warehouses.setOnClickListener {
            openMap(selectedItems)
        }
    }

    open fun openMap(selectedItems: List<WarehouseItem>) {
        val action = AddProductFragmentDirections.actionToMaps(selectedItems.toTypedArray())
        navController.navigate(action)
    }

    protected fun closeScreen() {
        val imm =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)

        navController.popBackStack()
    }

    private fun save(newTitle: String, newPrice: Double) {
        val newItem = ProductItem()
        newItem.title = newTitle
        newItem.price = newPrice
        newItem.photo = picturePath
        val selected = selectWarehouses.selectedItems
        productsViewModel.insert(newItem, selected)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, pickImageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) when (requestCode) {
            pickImageRequestCode -> {
                val selectedImage: Uri = data?.data!!
                picturePath = selectedImage.getPathString(context)
                val imageFile = File(picturePath)
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                image.setImageBitmap(bitmap)
            }
        }
    }

    private fun Uri.getPathString(context: Context?): String {
        var path = ""

        if (context != null) {
            context.contentResolver.query(
                this, arrayOf(MediaStore.Images.Media.DATA),
                null, null, null
            )?.apply {
                val columnIndex = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                moveToFirst()
                path = getString(columnIndex)
                close()
            }
        }
        return path
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(
        permissions: Array<String?>,
        requestCode: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                context?.let {
                    checkSelfPermission(
                        it,
                        permission
                    )
                } == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionRequestCode -> if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                pickImage()
            }
        }
    }

    companion object {
        private const val pickImageRequestCode = 100
        private const val permissionRequestCode = 101
    }
}
