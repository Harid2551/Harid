package com.harid.product_management

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int,
    val imagePath: String?
)
