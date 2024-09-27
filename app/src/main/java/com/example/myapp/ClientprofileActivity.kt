package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage

class ClientprofileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientprofile)

        val uid = intent.getStringExtra("uid")

        if(uid==null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val username:TextView = findViewById(R.id.usernamesection)
        val useremail:TextView = findViewById(R.id.useremail)
        val useruid:TextView = findViewById(R.id.useruid)
        val userpn:TextView = findViewById(R.id.userphonenumber)
        val userdisc:TextView = findViewById(R.id.storedisc)
        val userstore:TextView = findViewById(R.id.storenamesection)


        show_user_details(uid,username,userstore,userdisc,useremail,userpn,useruid)

    }

    private fun show_user_details(
        userId: String?,
        username: TextView,
        userstore: TextView,
        userdisc: TextView,
        useremail: TextView,
        userpn: TextView,
        useruid: TextView
    ) {
        if (userId != null) {

            Log.d("Curren user id", userId!!)

            val firestore = FirebaseFirestore.getInstance()

            firestore.collection("Users").document(userId!!).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {

                        val userData = document.data
                        username.text = userData?.get("username") as String
                        userstore.text = userData?.get("storename") as String
                        userdisc.text = userData?.get("disc") as String
                        useremail.text = userData?.get("email") as String
                        userpn.text = userData?.get("phone number") as String
                        useruid.text = userData?.get("userid") as String
                        loaduserprofile(userId)

                    } else {
                        // Document does not exist or is null
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle errors here
                }
        } else {
            // User is not authenticated or UID is null
            // Handle this scenario accordingly
        }
    }

    private fun loaduserprofile(userId: String){

        val profilephoto: ShapeableImageView = findViewById(R.id.userprofilephoto)

        val storageRef = Firebase.storage.reference
        val profileImageRef = storageRef.child("Images/$userId")
        Log.d("mainp", userId)

        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this).load(uri).into(profilephoto)
        }

    }
}