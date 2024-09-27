package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.myapp.preferences.PreferencesManager
import com.google.firebase.auth.FirebaseAuth

class MainclientActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferencesManager
    private var auth = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainclient)

        val uid = auth.uid
        val uid1 = intent.getStringExtra("uid")
        if (uid != null) {
            Log.d("uid",uid)
        }

        if (uid == null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        val profilebutton:ImageView = findViewById(R.id.profilebutton)
        val ordersbutton:ImageView = findViewById(R.id.customerorders)
        val marketbutton:ImageView = findViewById(R.id.orderedbuttons)
        val settings:ImageView = findViewById(R.id.settings)

        // Working Space

        profilebutton.setOnClickListener {

            val intent = Intent(this,ClientprofileActivity::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)

        }

        ordersbutton.setOnClickListener {

            val intent = Intent(this,ClicusordersActivity::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)

        }

        marketbutton.setOnClickListener {
            val intent = Intent(this,MarketActivity::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this,SettingsActivity::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
            finish()

        }

    }
}