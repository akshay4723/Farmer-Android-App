package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapp.preferences.LoadingDialog
import com.example.myapp.preferences.PreferencesManager
import com.example.myapp.preferences.checkcc
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class ClientalreadyhaveanaccountActivity : AppCompatActivity() {

    private lateinit var loading : LoadingDialog
    private lateinit var preferenceManager: PreferencesManager
    private var auth = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientalreadyhaveanaccount)

        loading = LoadingDialog(this)
        preferenceManager = PreferencesManager(this)

        val email1:EditText = findViewById(R.id.loginemail)
        val password1:EditText = findViewById(R.id.loginpassword)
        val login:Button = findViewById(R.id.login)

        login.setOnClickListener {

            val email = email1.text.toString()
            val password = password1.text.toString()

            if("@gmail.com" in email){
                val len = password.length
                if(password == ""){
                    Toast.makeText(this, "Please Enter the password", Toast.LENGTH_SHORT).show()
                }
                else if(len <8){
                    Toast.makeText(this, "Please Enter the password", Toast.LENGTH_SHORT).show()
                }
                else{
                    loading.startloading()
                    login(email, password)
                }
            }
            else {
                Toast.makeText(this, "Please fill it properly", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun login(email:String,password:String ){
        auth.signInWithEmailAndPassword(email,password).
        addOnCompleteListener {
            if(it.isSuccessful){
                preferenceManager.setLoggedIn(true)
                loading.stoploading()
                val intent = Intent(this,MainclientActivity::class.java)
                startActivity(intent)
                val checkccInstance = checkcc(this)
                checkccInstance.checking("client")
                finish()
            }else{
                Toast.makeText(this,"Something went Wrong",Toast.LENGTH_SHORT).show()
            }
        }.
        addOnFailureListener {
            loading.stoploading()
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }

    }
}