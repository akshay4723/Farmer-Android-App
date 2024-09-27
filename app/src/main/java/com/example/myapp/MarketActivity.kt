package com.example.myapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.myapp.dataclasses.Productpurchase
import com.google.firebase.database.FirebaseDatabase

class MarketActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)


        val uid = intent.getStringExtra("uid")

        val productname:EditText = findViewById(R.id.updateproductname)
        val productdisc:EditText = findViewById(R.id.updateproductdisc)
        val update:Button = findViewById(R.id.productupdatebutton)

        update.setOnClickListener {
            val proname = productname.text.toString()
            val prodisc = productdisc.text.toString()
            val uid1 = uid
            if (uid1 != null) {
                updateproduct(proname,prodisc,uid1)
            }

        }

    }

    private fun updateproduct(productname: String, productdisc: String, uid: String){

        val reference = FirebaseDatabase.getInstance().getReference("/clientproducts/productnames/$uid").push()

        val reference2 = FirebaseDatabase.getInstance().getReference("/products").push()

        val productmessage = Productpurchase(reference.key!!,productname,productdisc, uid)

        reference.setValue(productmessage)
            .addOnSuccessListener {
                // Log success message if needed
            }

        reference2.setValue(productmessage)
            .addOnSuccessListener {
                // Log success message if needed
            }


    }
}