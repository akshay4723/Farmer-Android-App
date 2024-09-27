package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MaincustomerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maincustomer)


        val searchbutton:ImageView = findViewById(R.id.customerorders)
        val myorders:ImageView = findViewById(R.id.orderedbuttons)
        val mycart:ImageView = findViewById(R.id.mycart)
        val signout:ImageView = findViewById(R.id.settings)


        searchbutton.setOnClickListener {
            val intent = Intent(this,ClientsearchActivity::class.java)
            startActivity(intent)
        }

        myorders.setOnClickListener {
            val intent = Intent(this,CustomersordersActivity::class.java)
            startActivity(intent)
        }

        mycart.setOnClickListener {
            val intent = Intent(this,CustomercartsectionActivity::class.java)
            startActivity(intent)
        }

        signout.setOnClickListener {
            val intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}