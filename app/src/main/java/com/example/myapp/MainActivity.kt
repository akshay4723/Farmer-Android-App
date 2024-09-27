package com.example.myapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.myapp.preferences.PreferencesManager
import com.example.myapp.preferences.checkcc

class MainActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferencesManager
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE)
        preferenceManager = PreferencesManager(this)

        if(preferenceManager.isLoggedIn()){

            val checkccInstance = checkcc(this)
            val result = checkccInstance.letscheck()
            Log.d("ccccccccc","$result")
            if(result == 89){
                val intent = Intent(this,MainclientActivity::class.java)
                startActivity(intent)
                finish()

            }
            else{

                val intent = Intent(this,MaincustomerActivity::class.java)
                startActivity(intent)
                finish()

            }
        }

        val client_button :Button = findViewById(R.id.client)
        val customer_Button:Button = findViewById(R.id.customer)

        client_button.setOnClickListener {
            val intent = Intent(this,ClientActivity::class.java)
            startActivity(intent)
            finish()
        }

        customer_Button.setOnClickListener {
            val intent = Intent(this,CustomerActivity::class.java)
            startActivity(intent)
        }

    }
}