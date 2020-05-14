package ru.ecwid.testapp.ui


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.product_item.view.*
import ru.ecwid.testapp.R
import ru.ecwid.testapp.models.ProductItem
import ru.ecwid.testapp.ui.ProductsListFragment.OnListFragmentInteractionListener
import java.io.File


class ProductsAdapter(
    private var values: List<ProductItem>,
    private val listener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ProductItem
            listener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text = item.title
        val price = item.price.toString()
        val formattedPrice = "$price руб."
        holder.price.text = formattedPrice

        if (!item.photo.isBlank()) {
            val imageFile = File(item.photo)
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            holder.image.setImageBitmap(bitmap)
        }

        with(holder.view) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = values.size

    fun setItems(items: List<ProductItem>) {
        values = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.title
        val price: TextView = view.price
        val image: ImageView = view.image
    }
}
