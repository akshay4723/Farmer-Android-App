package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.myapp.preferences.PreferencesManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SettingsActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        preferenceManager = PreferencesManager(this)

        val signout:TextView = findViewById(R.id.signout)


        signout.setOnClickListener {
            signout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun signout() {
        val id = FirebaseAuth.getInstance().currentUser?.uid

        val mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        preferenceManager.setLoggedIn(false)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this,MainclientActivity::class.java)
        startActivity(intent)
        finish()
    }
}