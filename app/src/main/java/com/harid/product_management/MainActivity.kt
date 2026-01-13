package com.harid.product_management

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var dbHelper: ProductDbHelper
    private lateinit var fabAddProduct: FloatingActionButton
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupToolbar()
        setupRecyclerView()
        loadProducts()

        fabAddProduct.setOnClickListener {
            val intent = Intent(this, AddEditProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.reclerViewProduct)
        fabAddProduct = findViewById(R.id.fabAddProduct)
        dbHelper = ProductDbHelper(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "จัดการสินค้า"
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            products = emptyList(),
            onEditClick = { product ->
                val intent = Intent(this, AddEditProductActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                startActivity(intent)
            },
            onDeleteClick = { product ->
                showDeleteConfirmDialog(product)
            },
            onItemClick = { product ->
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                startActivity(intent)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = productAdapter
    }

    private fun loadProducts() {
        val products = dbHelper.getAllProducts()
        productAdapter.updateProducts(products)
    }

    private fun showDeleteConfirmDialog(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("ยืนยันการลบ")
            .setMessage("คุณต้องการลบ ${product.name} ใช่หรือไม่?")
            .setPositiveButton("ลบ") { _, _ ->
                deleteProduct(product)
            }
            .setNegativeButton("ยกเลิก", null)
            .show()
    }

    private fun deleteProduct(product: Product) {
        val result = dbHelper.deleteProduct(product.id)
        if (result > 0) {
            Toast.makeText(this, "ลบสินค้าสำเร็จ", Toast.LENGTH_SHORT).show()
            loadProducts()
        } else {
            Toast.makeText(this, "ลบสินค้าไม่สำเร็จ", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }
}