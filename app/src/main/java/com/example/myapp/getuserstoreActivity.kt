package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class getuserstoreActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getuserstore)

        //Getting from main signin layout

        val email = intent.getStringExtra("email")
        val pn = intent.getStringExtra("pn")
        val pass = intent.getStringExtra("pass")

        //Getting from user interact

        val username:EditText =findViewById(R.id.username)
        val storename:EditText = findViewById(R.id.storename)
        val dis:EditText = findViewById(R.id.dis)
        val next:Button = findViewById(R.id.next)

        next.setOnClickListener {

            val usern = username.text.toString()
            val storen = storename.text.toString()
            val disc = dis.text.toString()

            if (usern != ""){
                if (storen!=""){
                    val intent = Intent(this,ProfilephotoActivity::class.java)
                    intent.putExtra("username",usern)
                    intent.putExtra("storename",storen)
                    intent.putExtra("disc",disc)
                    intent.putExtra("email",email)
                    intent.putExtra("pn",pn)
                    intent.putExtra("pass",pass)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }
}