package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapp.adapters.ClicustomerordersAdapters
import com.example.myapp.dataclasses.createorderstatus
import com.example.myapp.dataclasses.shocustomersorders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.Manifest
import android.widget.ImageView

class ReadyordersActivity : AppCompatActivity() {

    private val REQUEST_CALL_PERMISSION = 1
    private val phoneNumber = "8124498491"

    private var productList: MutableList<shocustomersorders> = mutableListOf()
    private lateinit var productAdapter: ClicustomerordersAdapters

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_readyorders)

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        val customeruid = intent.getStringExtra("customeruid")
        val key = intent.getStringExtra("key")
        val pn = intent.getStringExtra("pn")
        Log.d("ref",key.toString())

        val readybutton:Button = findViewById(R.id.orderready)
        val makecall:ImageView = findViewById(R.id.callbutton)

        readybutton.setOnClickListener {
            orderready(customeruid.toString(),key.toString(),uid)
        }

        makecall.setOnClickListener {
            makePhoneCall(phoneNumber)
        }

    }

    private fun orderready(uid: String, key: String, uid1: String?) {

        if (uid1 != null) {

            val ref = FirebaseDatabase.getInstance().getReference("/customer-orders-status/$uid1/$key")

            val currentstatus = createorderstatus("Finish")

            ref.setValue(currentstatus)
                .addOnSuccessListener {
                    // Log success message if needed
                }

        }
    }

    private fun makePhoneCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission was granted
            makePhoneCall(phoneNumber)
        } else {
            // Permission was denied
            // Show some message to the user
        }
    }
}