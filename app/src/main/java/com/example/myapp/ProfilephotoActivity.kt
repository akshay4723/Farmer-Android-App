package com.example.myapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.myapp.dataclasses.Userprofileurl
import com.example.myapp.preferences.LoadingDialog
import com.example.myapp.preferences.PreferencesManager
import com.example.myapp.preferences.checkcc
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfilephotoActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferencesManager
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var loading : LoadingDialog

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilephoto)

        loading = LoadingDialog(this)

        val profilephoto:ShapeableImageView = findViewById(R.id.profilephoto)
        val signin:Button = findViewById(R.id.signinbutton)

        val username = intent.getStringExtra("username")
        val storename = intent.getStringExtra("storename")
        val discription = intent.getStringExtra("disc")
        val email = intent.getStringExtra("email")
        val pn = intent.getStringExtra("pn")
        val pass = intent.getStringExtra("pass")

        preferenceManager = PreferencesManager(this)

        profilephoto.setOnClickListener {
            Toast.makeText(this,"Please select the image with 1:1 ratio", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

        signin.setOnClickListener {
            loading.startloading()
            signup_procedures(username.toString(), storename.toString(), discription.toString(), email.toString(), pn.toString(), pass.toString(), profilephoto)
        }

    }

    var selectedPhotoUri: Uri?=null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && resultCode == Activity.RESULT_OK && data!=null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            val profilephoto: ShapeableImageView = findViewById(R.id.profilephoto)

            val bitmapdrawable = BitmapDrawable(bitmap)
            profilephoto.setBackgroundDrawable(bitmapdrawable)
        }
    }

    // Sign up Procedures

    private fun signup_procedures(username: String, storename:String, disc:String,email:String, pn:String, pass:String, profilephoto:ShapeableImageView) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    if (user != null) {
                        uploadprofiletofirbasestorage(user.uid,username, storename, disc, email, pn, pass,profilephoto)
                    }
                }
            }
    }

    // Upload Image to Firebase

    private fun uploadprofiletofirbasestorage(userid:String,username: String,storename: String,disc:String, email:String, pn:String, pass: String,profilephoto: ShapeableImageView) {

        if (selectedPhotoUri == null) {
            Log.d("profile error", "Please set the profile photo")
        } else {
            val filename = userid
            val ref = FirebaseStorage.getInstance().getReference("/Images/$filename")

            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("main", "Created with ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("main", "Location $it")

                        val ref =
                            FirebaseDatabase.getInstance().getReference("/user-profile/$userid")

                        val profileurl = Userprofileurl("$it")

                        ref.setValue(profileurl).addOnSuccessListener {

                        }

                        Create_user_Signin(userid, username, storename, disc, email, pn, pass, it.toString())

                    }
                }
        }
    }

    private fun Create_user_Signin(userid: String,username: String,storename: String, disc: String, email: String, pn: String, pass: String,profilephoto:String){
        val hashmap = hashMapOf(
            "userid" to userid,
            "username" to username,
            "storename" to storename,
            "disc" to disc,
            "email" to email,
            "phone number" to pn,
            "profilephotourl" to profilephoto
        )

        firestore.collection("Users").document(userid).set(hashmap)
            .addOnSuccessListener {
                makeuserprofileurl(userid,profilephoto)
                Log.d("main","${userid}")
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun makeuserprofileurl(userid:String,profilephoto:String){

        val ref = FirebaseDatabase.getInstance().getReference("/user-profile/$userid")

        val profileurl = Userprofileurl("$profilephoto")

        ref.setValue(profileurl).addOnSuccessListener {
            val intent = Intent(this, MainclientActivity::class.java)
            intent.putExtra("uid",userid)
            startActivity(intent)
            preferenceManager.setLoggedIn(true)
            loading.stoploading()
            val checkccInstance = checkcc(this)
            checkccInstance.checking("client")
            finish()
        }

    }
}