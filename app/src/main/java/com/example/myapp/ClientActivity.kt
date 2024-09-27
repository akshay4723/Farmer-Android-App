package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class ClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        val email:EditText = findViewById(R.id.gmail)
        val phonenumber:EditText = findViewById(R.id.phonenumber)
        val password:EditText = findViewById(R.id.password)
        val signin:Button = findViewById(R.id.signin)
        val alreadyhaveanaccount:TextView = findViewById(R.id.alreadyhaveaccount)

        signin.setOnClickListener {

            val email1 = email.text.toString()
            val pn1 = phonenumber.text.toString()
            val pass1 = password.text.toString()

            if (pn1!="" && pn1.length == 10) {
                if("@gmail.com" in email1){
                    val len = pass1.length
                    if(pass1 == ""){
                        Toast.makeText(this, "Please Enter the password", Toast.LENGTH_SHORT).show()
                    }
                    else if(len <8){
                        Toast.makeText(this, "Please Enter the password", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val intent = Intent(this,getuserstoreActivity::class.java)
                        intent.putExtra("email",email1)
                        intent.putExtra("pn",pn1)
                        intent.putExtra("pass",pass1)
                        startActivity(intent)
                        finish()
                    }
                }
                else {
                    Toast.makeText(this, "Please fill it properly", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Log.d("Error","Please Enter a valid Phone number")
            }

        }

    alreadyhaveanaccount.setOnClickListener {
        val intent = Intent(this,ClientalreadyhaveanaccountActivity::class.java)
        startActivity(intent)
        finish()
    }

    }
}








