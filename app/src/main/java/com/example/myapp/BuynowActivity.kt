package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.myapp.dataclasses.createorderstatus
import com.example.myapp.dataclasses.productbuy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BuynowActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buynow)


        val myuid = FirebaseAuth.getInstance().currentUser?.uid
        val storeuid = intent.getStringExtra("clickeduid")
        val proname = intent.getStringExtra("proname")
        val prodisc = intent.getStringExtra("prodisc")
        val storename = intent.getStringExtra("storename")
        if (storeuid != null) {
            Log.d("test",storeuid)
        }


        val name:EditText = findViewById(R.id.buyname)
        val address:EditText = findViewById(R.id.buyaddress)
        val phonenumber:EditText = findViewById(R.id.buyphonenumber)
        val buy:Button = findViewById(R.id.buybutton)

        buy.setOnClickListener {
            buyproduct(name.text.toString(),address.text.toString(),phonenumber.text.toString(),storeuid,myuid,proname,prodisc,storename)
        }

    }

    private fun buyproduct(
        name: String,
        address: String,
        pn: String,
        storeuid: String?,
        myuid: String?,
        proname: String?,
        prodisc: String?,
        storename: String?
    ) {

        val reference = FirebaseDatabase.getInstance().getReference("/customers-buy-products/$myuid").push()

        val reference2 = FirebaseDatabase.getInstance().getReference("/customer-orders/$storeuid").push()

        val key = reference2.key

        val productmessage = productbuy(name,address,pn,storeuid.toString(),myuid.toString(),proname.toString(),prodisc.toString(),storename.toString(),key.toString())

        val reference3 = FirebaseDatabase.getInstance().getReference("/customer-orders-status/$storeuid/$key")

        val currentstatus = createorderstatus("Running")

        reference3.setValue(currentstatus).addOnSuccessListener {

        }

        reference.setValue(productmessage)
            .addOnSuccessListener {
                val intent = Intent(this,OrderedsuccessActivity::class.java)
                startActivity(intent)
                finish()
            }

        reference2.setValue(productmessage)
            .addOnSuccessListener {
                // Log success message if needed
            }

    }
}