package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.adapters.ProductAdapters
import com.example.myapp.products.Searchproduct
import com.google.firebase.database.*

class ClientsearchActivity : AppCompatActivity() {

    private var productList: MutableList<Searchproduct> = mutableListOf()
    private lateinit var productAdapter: ProductAdapters

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientsearch)

        val searchbar: EditText = findViewById(R.id.searchbar)
        val recyclerView: RecyclerView = findViewById(R.id.searchedproduct)

        // Initialize RecyclerView and Adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapters(productList) { product ->
            Log.d("after clicked", "Product clicked: ${product.productname}")
            val intent = Intent(this,ProductclickedActvity::class.java)
            intent.putExtra("uid",product.uid)
            startActivity(intent)
            // Handle item click event
            finish()
        }
        recyclerView.adapter = productAdapter

        searchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                searchProducts(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun searchProducts(query: String) {
        if (query.isBlank()) {
            productList.clear()
            productAdapter.notifyDataSetChanged()
            return
        }

        val productsRef = FirebaseDatabase.getInstance().getReference("/products")

        productsRef.orderByChild("productname")
            .startAt(query)
            .endAt(query + "\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productList.clear()
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Searchproduct::class.java)
                        product?.let {
                            productList.add(it)
                            Log.d("SearchProducts", "Product found: ${it.productname}")
                        }
                    }
                    productAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("SearchProducts", "Database error: ${error.message}")
                }
            })
    }
}
