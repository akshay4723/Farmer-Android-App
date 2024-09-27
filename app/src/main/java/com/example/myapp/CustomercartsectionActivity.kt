package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.adapters.CarttobuyAdapters
import com.example.myapp.adapters.ProductAdapters
import com.example.myapp.dataclasses.showingcartstobuy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class CustomercartsectionActivity : AppCompatActivity() {

    private var productList: MutableList<showingcartstobuy> = mutableListOf()
    private lateinit var productAdapter: CarttobuyAdapters


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customercartsection)

        val uid = FirebaseAuth.getInstance().currentUser?.uid


        val recyclerView:RecyclerView = findViewById(R.id.productssection)

        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = CarttobuyAdapters(productList) { product ->

            val intent = Intent(this,BuynowActivity::class.java)
            intent.putExtra("clickeduid",product.storeuid)
            intent.putExtra("proname",product.productname)
            intent.putExtra("prodisc",product.productdisc)
            intent.putExtra("storename",product.storename)
            startActivity(intent)

            Log.d("Success","")

        }
        recyclerView.adapter = productAdapter

        if (uid != null) {
            loadtcartprodcuts(uid)
        }


    }

    private fun loadtcartprodcuts(uid:String) {
        val recyclerView: RecyclerView = findViewById(R.id.productssection)
        if (uid != null) {

            val ref = FirebaseDatabase.getInstance().getReference("/customer-carts/$uid")

            ref.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val cart = snapshot.getValue(showingcartstobuy::class.java)
                    cart?.let {
                        productList.add(it)
                        recyclerView.scrollToPosition(productList.size - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            })
        }
    }

}
