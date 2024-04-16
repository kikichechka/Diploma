package com.example.taskplanner.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.TypeNotes
import com.example.taskplanner.databinding.ItemTaskProductsBinding
import com.example.taskplanner.databinding.ItemTaskReminderBinding
import com.example.taskplanner.databinding.ItemTypeProductBinding

class ProductsListAdapter(
    private val onLongClickChangeOneProductFinished: (Products, Int) -> Unit,
    private val products: Products,
) : RecyclerView.Adapter<ProductsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(ItemTypeProductBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return products.listProducts.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val itemNote = products.listProducts[position]
        holder.binding.titleProduct.text = itemNote
        holder.binding.root.setOnLongClickListener {
            onLongClickChangeOneProductFinished(products, position)
            true
        }
    }
}

class ProductsViewHolder(val binding: ItemTypeProductBinding) : ViewHolder(binding.root)
