package com.example.myapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
import java.io.ByteArrayOutputStream

class CustomerActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var preferenceManager: PreferencesManager
    private lateinit var loading:LoadingDialog

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        loading = LoadingDialog(this)
        sharedPreferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        preferenceManager = PreferencesManager(this)

        if (preferenceManager.isLoggedIn()) {
            startActivity(Intent(this, MaincustomerActivity::class.java))
            finish()
        }

        val alreadyhaveanaccount: TextView = findViewById(R.id.textView3)

        val usernametext = findViewById<EditText>(R.id.editTextText_username)
        val emailtext = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordtext = findViewById<EditText>(R.id.editTextTextPassword)
        val profilephoto: ShapeableImageView = findViewById(R.id.imageView)
        val register: Button = findViewById(R.id.registerbutton)

        alreadyhaveanaccount.setOnClickListener {
            val intent = Intent(this, CustomerloginActivity::class.java)
            startActivity(intent)
            finish()
        }

        profilephoto.setOnClickListener {
            Toast.makeText(this,"Please select the image with 1:1 ratio", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

        register.setOnClickListener {

            val username = usernametext.text.toString()
            val email = emailtext.text.toString()
            val password = passwordtext.text.toString()

            Log.d("main", "$username $password $email")

            val text1 = Regex("[A-Z]")
            val text2 = Regex("[^A-Za-z0-9 ]")

            if (text1.containsMatchIn(username)) {
                Toast.makeText(this, "Caps, space and symbols are not allowed", Toast.LENGTH_SHORT).show()
            }

            else if(username == ""){
                Toast.makeText(this, "Caps, space and symbols are not allowed", Toast.LENGTH_SHORT).show()
            }
            else if(" " in username){
                Toast.makeText(this, "Caps, space and symbols are not allowed", Toast.LENGTH_SHORT).show()
            }
            else if(text2.containsMatchIn(username)){
                Toast.makeText(this, "Caps, space and symbols are not allowed", Toast.LENGTH_SHORT).show()
            }

            else {
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
                        signup_procedures(username, email, password,profilephoto)
                    }
                }
                else {
                    Toast.makeText(this, "Please fill it properly", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun signup_procedures(username: String, email: String, password: String,profilephoto: ShapeableImageView) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    if (user != null) {
                        uploadprofiletofirbasestorage(user.uid,username,email,profilephoto)
                    }
                }
            }
    }

    private fun goToMainActivity(){
        val intent = Intent(this, MaincustomerActivity::class.java)
        startActivity(intent)
    }

    var selectedPhotoUri: Uri?=null
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && resultCode == Activity.RESULT_OK && data!=null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            val profilephoto: ShapeableImageView = findViewById(R.id.imageView)

            val bitmapdrawable = BitmapDrawable(bitmap)
            profilephoto.setBackgroundDrawable(bitmapdrawable)
        }
    }

    private fun uploadprofiletofirbasestorage(userid:String,username: String,useremail: String,profilephoto: ShapeableImageView){

        if (selectedPhotoUri == null){
            val filename = userid
            val ref = FirebaseStorage.getInstance().getReference("/Images/$filename")

            val drawable = profilephoto.background
            val bitmap = (drawable as BitmapDrawable).bitmap

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            ref.putBytes(data)
                .addOnSuccessListener {
                    Log.d("main","Created with ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("main","Location $it")

                        Create_user_Signin(userid,username,useremail,it.toString())

                    }
                }
        }

        else{
            val filename = userid
            val ref = FirebaseStorage.getInstance().getReference("/Images/$filename")

            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("main","Created with ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("main","Location $it")

                        val ref = FirebaseDatabase.getInstance().getReference("/customer-profile/$userid")

                        val profileurl = Userprofileurl("$it")

                        ref.setValue(profileurl).addOnSuccessListener {

                        }

                        Create_user_Signin(userid,username,useremail,it.toString())

                    }
                }
        }
    }

    private fun Create_user_Signin(userid: String,username: String,useremail:String,profilephoto:String){
        val hashmap = hashMapOf(
            "userid" to userid,
            "username" to username,
            "useremail" to useremail,
            "status" to "default",
            "imageurl" to profilephoto
        )

        firestore.collection("Customers").document(userid).set(hashmap)
            .addOnSuccessListener {
                makeuserprofileurl(userid,profilephoto)
                Log.d("main","${userid}")
                preferenceManager.setLoggedIn(true)
                loading.stoploading()
                val checkccInstance = checkcc(this)
                checkccInstance.checking("customer")
                goToMainActivity()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
    private fun makeuserprofileurl(userid:String,profilephoto:String){

        val ref = FirebaseDatabase.getInstance().getReference("/customer-profile/$userid")

        val profileurl = Userprofileurl(profilephoto)

        ref.setValue(profileurl).addOnSuccessListener {

        }

    }

}
