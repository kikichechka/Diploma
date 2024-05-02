package com.example.taskplanner.presentation.adapters

import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.databinding.ItemTypeProductBinding

class ProductsListAdapter(
    private val onLongClickChangeOneProductFinished: (Product) -> Unit,
    private val product: MutableList<Product>,
) : RecyclerView.Adapter<ProductsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(ItemTypeProductBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return product.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val itemNote = product[position]
        if (!itemNote.finished) {
            holder.binding.titleProduct.text = itemNote.title
        } else {
            holder.binding.titleProduct.text = createSpanned(itemNote.title)
        }
        holder.binding.root.setOnLongClickListener {
            onLongClickChangeOneProductFinished(itemNote)
            true
        }
    }

    private fun createSpanned(title: String): Spanned {
        return HtmlCompat.fromHtml(
            ("<strike>$title</strike>"),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}

class ProductsViewHolder(val binding: ItemTypeProductBinding) : ViewHolder(binding.root)
