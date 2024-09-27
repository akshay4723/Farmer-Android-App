package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.adapters.CarttobuyAdapters
import com.example.myapp.adapters.ClicustomerordersAdapters
import com.example.myapp.dataclasses.shocustomersorders
import com.example.myapp.dataclasses.showingcartstobuy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ClicusordersActivity : AppCompatActivity() {

    private var productList: MutableList<shocustomersorders> = mutableListOf()
    private lateinit var productAdapter: ClicustomerordersAdapters

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicusorders)

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        val recyclerView:RecyclerView = findViewById(R.id.customerorderslist)

        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ClicustomerordersAdapters(productList) { product ->

            val intent = Intent(this,ReadyordersActivity::class.java)
            Log.d("user uid",product.myuid)
            Log.d("key",product.referencekey)
            intent.putExtra("customeruid",product.myuid)
            intent.putExtra("key",product.referencekey)
            intent.putExtra("pn",product.phonenumber)
            startActivity(intent)

        }
        recyclerView.adapter = productAdapter

        if (uid != null) {
            loadcustomerorders(uid)
        }

    }

    private fun loadcustomerorders(uid:String) {
        val recyclerView: RecyclerView = findViewById(R.id.customerorderslist)
        if (uid != null) {

            val ref = FirebaseDatabase.getInstance().getReference("/customer-orders/$uid")

            ref.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val cart = snapshot.getValue(shocustomersorders::class.java)
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