package com.harid.product_management

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private var products: List<Product>,
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewProduct)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        val textViewQuantity: TextView = itemView.findViewById(R.id.textViewQuantity)
        val buttonEdit: Button = itemView.findViewById(R.id.buttonEdit)
        val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.textViewName.text = product.name

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("th", "TH"))
        holder.textViewPrice.text = "ราคา: ${currencyFormat.format(product.price)}"

        holder.textViewQuantity.text = "จำนวน: ${product.quantity} ชิ้น"

        if (!product.imagePath.isNullOrEmpty()) {
            holder.imageView.setImageURI(Uri.parse(product.imagePath))
        } else {
            holder.imageView.setImageResource(R.drawable.ic_image_placeholder)
        }

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }

        holder.buttonEdit.setOnClickListener {
            onEditClick(product)
        }

        holder.buttonDelete.setOnClickListener {
            onDeleteClick(product)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}