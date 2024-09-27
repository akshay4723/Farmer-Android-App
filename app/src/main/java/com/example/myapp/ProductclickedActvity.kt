package com.example.myapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.dataclasses.cartdetails
import com.example.myapp.dataclasses.loadproductdetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ProductclickedActvity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private var mainusername:String = ""
    private var mainemail:String = ""
    private var mainphonenumber:String = ""
    private var mainstorename:String = ""
    private var mainproductname:String = ""
    private var mainproductdisc:String = ""


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productclicked_actvity)

        val clickeduid = intent.getStringExtra("uid")

        val storename:TextView = findViewById(R.id.pcstorename)
        val username:TextView = findViewById(R.id.pcusername)
        val email:TextView = findViewById(R.id.pcemail)
        val location:TextView = findViewById(R.id.pclocation)
        val phonenumber:TextView = findViewById(R.id.pcphonenumber)
        val productname:TextView = findViewById(R.id.pcproductname)
        val productdisc:TextView = findViewById(R.id.pcproductdisc)
        val addatocart:Button = findViewById(R.id.addtocart)

        load_store_details(clickeduid.toString(),email,username,storename,location,phonenumber)
        load_product_details(clickeduid.toString(),productname,productdisc)


        addatocart.setOnClickListener {
            val currentuid = auth.currentUser?.uid
            addingtocart(currentuid.toString(),clickeduid.toString())
        }


    }

    private fun load_store_details(
        uid: String,
        email:TextView,
        username:TextView,
        storename: TextView,
        location: TextView,
        phonenumber: TextView
    ){

        if (uid != null) {

            Log.d("Curren user id", uid!!)

            val firestore = FirebaseFirestore.getInstance()

            firestore.collection("Users").document(uid!!).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {

                        val userData = document.data
                        username.text = userData?.get("username") as String
                        storename.text = userData?.get("storename") as String
                        phonenumber.text = userData?.get("phone number") as String
                        email.text = userData?.get("email") as String

                        mainusername = userData?.get("username") as String
                        mainstorename = userData?.get("storename") as String
                        mainphonenumber = userData?.get("phone number") as String
                        mainemail = userData?.get("email") as String

                    } else {
                        // Document does not exist or is null
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle errors here
                }
        } else {
            // User is not authenticated or UID is null
            // Handle this scenario accordingly
        }

    }

    private fun load_product_details(uid: String, productname: TextView, productdisc: TextView){

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("clientproducts/productnames/$uid")

        Log.d("Loadproduct","$uid")
        // Attach a listener to read the data
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val prodet = snapshot.getValue(loadproductdetails::class.java)
                prodet?.let {
                    productname.text = it.productname
                    productdisc.text = it.productdisc

                    mainproductname = it.productname
                    mainproductdisc = it.productdisc
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

    private fun addingtocart(uid: String, clickeduid: String){

        val reference = FirebaseDatabase.getInstance().getReference("/customer-carts/$uid").push()

        if(mainusername!=null){

            val cutomercart = cartdetails(mainusername,mainstorename,mainphonenumber,mainemail,mainproductname,mainproductdisc,clickeduid)

            reference.setValue(cutomercart)
                .addOnSuccessListener {
                    Log.d("LOOG","SUCCESS")
                    Toast.makeText(this,"Added to cart",Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener{
                    Log.d("this","$it")
                }
        }
    }
}