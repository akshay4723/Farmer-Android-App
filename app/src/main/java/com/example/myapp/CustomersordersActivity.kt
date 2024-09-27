package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.adapters.CarttobuyAdapters
import com.example.myapp.adapters.ShowordersAdapters
import com.example.myapp.dataclasses.showingcartstobuy
import com.example.myapp.dataclasses.showingmyorders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class CustomersordersActivity : AppCompatActivity() {

    private var productList: MutableList<showingmyorders> = mutableListOf()
    private lateinit var productAdapter: ShowordersAdapters


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customersorders)


        val myuid = FirebaseAuth.getInstance().currentUser?.uid

        val recyclerView:RecyclerView = findViewById(R.id.ordersrecycleview)

        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ShowordersAdapters(productList) { product ->

            Log.d("Success","")

        }
        recyclerView.adapter = productAdapter



        loadmyorders(myuid)

    }

    private fun loadmyorders(uid: String?) {

        val recyclerView:RecyclerView = findViewById(R.id.ordersrecycleview)
        Log.d("Have to see","$uid")

        if (uid != null) {

            val ref = FirebaseDatabase.getInstance().getReference("/customers-buy-products/$uid")

            ref.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val cart = snapshot.getValue(showingmyorders::class.java)
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