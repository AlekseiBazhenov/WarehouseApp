package ru.ecwid.testapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_products_list.*
import ru.ecwid.testapp.R
import ru.ecwid.testapp.models.ProductItem
import ru.ecwid.testapp.ui.viewmodels.ProductViewModel

class ProductsListFragment : Fragment() {

    private lateinit var navController: NavController

    private val productsViewModel by viewModels<ProductViewModel>()

    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true

        navController = findNavController(this)

        productsAdapter = ProductsAdapter(emptyList(), object : OnListFragmentInteractionListener {
            override fun onListFragmentInteraction(item: ProductItem) {
                val action = ProductsListFragmentDirections.actionEdit(item.productId)
                navController.navigate(action)
            }
        })

        addNew.setOnClickListener {
            val action = ProductsListFragmentDirections.actionAdd()
            navController.navigate(action)
        }

        list.adapter = productsAdapter

        productsViewModel.getAllProducts().observe(
            viewLifecycleOwner,
            Observer(productsAdapter::setItems)
        )
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: ProductItem)
    }
}
