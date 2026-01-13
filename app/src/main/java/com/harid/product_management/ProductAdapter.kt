package com.harid.product_management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vachara.booking.databinding.ItemProductBinding

class ProductAdapter(
    private var products: List<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {

            if (!product.imagePath.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(product.imagePath)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(binding.imageViewProduct)
            } else {
                binding.imageViewProduct
                    .setImageResource(R.drawable.ic_image_placeholder)
            }

            binding.textViewName.text = product.name
            binding.textViewPrice.text = "ราคา: ${product.price} บาท"
            binding.textViewQuantity.text = "จำนวน: ${product.quantity} ชิ้น"

            binding.root.setOnClickListener { onItemClick(product) }
            binding.buttonEdit.setOnClickListener { onEditClick(product) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}

