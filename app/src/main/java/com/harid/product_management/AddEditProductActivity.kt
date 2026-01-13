package com.harid.product_management

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class AddEditProductActivity : AppCompatActivity() {

    private lateinit var imageViewProduct: ImageView
    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var editTextQuantity: EditText
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonSave: Button
    private lateinit var dbHelper: ProductDbHelper

    private var selectedImageUri: Uri? = null
    private var productId: Int = -1
    private var isEditMode = false

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                imageViewProduct.setImageURI(uri)
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product)

        initViews()

        productId = intent.getIntExtra("PRODUCT_ID", -1)
        isEditMode = productId != -1

        if (isEditMode) {
            supportActionBar?.title = "แก้ไขสินค้า"
            loadProductData(productId)
        } else {
            supportActionBar?.title = "เพิ่มสินค้า"
        }

        buttonSelectImage.setOnClickListener {
            selectImage()
        }

        buttonSave.setOnClickListener {
            saveProduct()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {
        imageViewProduct = findViewById(R.id.imageViewProduct)
        editTextName = findViewById(R.id.editTextName)
        editTextDescription = findViewById(R.id.editTextDescription)
        editTextPrice = findViewById(R.id.editTextPrice)
        editTextQuantity = findViewById(R.id.editTextQuantity)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonSave = findViewById(R.id.buttonSave)
        dbHelper = ProductDbHelper(this)
    }

    private fun loadProductData(productId: Int) {
        val product = dbHelper.getProductById(productId)
        if (product != null) {
            editTextName.setText(product.name)
            editTextDescription.setText(product.description)
            editTextPrice.setText(product.price.toString())
            editTextQuantity.setText(product.quantity.toString())

            if (!product.imagePath.isNullOrEmpty()) {
                selectedImageUri = Uri.parse(product.imagePath)
                imageViewProduct.setImageURI(selectedImageUri)
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
        imagePickerLauncher.launch(intent)
    }

    private fun saveProduct() {
        val name = editTextName.text.toString().trim()
        val description = editTextDescription.text.toString().trim()
        val priceStr = editTextPrice.text.toString().trim()
        val quantityStr = editTextQuantity.text.toString().trim()

        if (name.isEmpty()) {
            editTextName.error = "กรุณากรอกชื่อสินค้า"
            return
        }

        if (priceStr.isEmpty()) {
            editTextPrice.error = "กรุณากรอกราคา"
            return
        }

        if (quantityStr.isEmpty()) {
            editTextQuantity.error = "กรุณากรอกจำนวน"
            return
        }

        val price = priceStr.toDoubleOrNull()
        if (price == null || price < 0) {
            editTextPrice.error = "กรุณากรอกราคาที่ถูกต้อง"
            return
        }

        val quantity = quantityStr.toIntOrNull()
        if (quantity == null || quantity < 0) {
            editTextQuantity.error = "กรุณากรอกจำนวนที่ถูกต้อง"
            return
        }

        val product = Product(
            id = if (isEditMode) productId else 0,
            name = name,
            description = description,
            price = price,
            quantity = quantity,
            imagePath = selectedImageUri?.toString()
        )

        if (isEditMode) {
            val result = dbHelper.updateProduct(product)
            if (result > 0) {
                Toast.makeText(this, "แก้ไขสินค้าสำเร็จ", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "แก้ไขสินค้าไม่สำเร็จ", Toast.LENGTH_SHORT).show()
            }
        } else {
            val result = dbHelper.addProduct(product)
            if (result > 0) {
                Toast.makeText(this, "เพิ่มสินค้าสำเร็จ", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "เพิ่มสินค้าไม่สำเร็จ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}