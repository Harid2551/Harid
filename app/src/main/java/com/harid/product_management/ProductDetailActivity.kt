package com.harid.product_management

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat
import java.util.Locale

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var imageViewProduct: ImageView
    private lateinit var textViewName: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var textViewPrice: TextView
    private lateinit var textViewQuantity: TextView
    private lateinit var dbHelper: ProductDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId != -1) {
            loadProductDetail(productId)
        } else {
            Toast.makeText(this, "ไม่พบข้อมูลสินค้า", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initViews() {
        imageViewProduct = findViewById(R.id.imageViewProduct)
        textViewName = findViewById(R.id.textViewName)
        textViewDescription = findViewById(R.id.textViewDescription)
        textViewPrice = findViewById(R.id.textViewPrice)
        textViewQuantity = findViewById(R.id.textViewQuantity)
        dbHelper = ProductDbHelper(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "รายละเอียดสินค้า"
    }

    private fun loadProductDetail(productId: Int) {
        val product = dbHelper.getProductById(productId)

        if (product != null) {
            textViewName.text = product.name
            textViewDescription.text = product.description

            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("th", "TH"))
            textViewPrice.text = "ราคา: ${currencyFormat.format(product.price)}"

            textViewQuantity.text = "จำนวนคงเหลือ: ${product.quantity} ชิ้น"

            if (!product.imagePath.isNullOrEmpty()) {
                imageViewProduct.setImageURI(Uri.parse(product.imagePath))
            } else {
                imageViewProduct.setImageResource(R.drawable.ic_image_placeholder)
            }
        } else {
            Toast.makeText(this, "ไม่พบข้อมูลสินค้า", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}