package com.example.myapp.preferences

import android.app.Activity
import android.app.AlertDialog
import com.example.myapp.R

class LoadingDialog(val mactivity: Activity) {

    private lateinit var dialog : AlertDialog


    fun startloading(){

        val inflater = mactivity.layoutInflater
        val dialogview = inflater.inflate(R.layout.loadingbox,null)

        // ==========



        // ==========
        val builder = AlertDialog.Builder(mactivity)
        builder.setView(dialogview)
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

    }

    fun stoploading(){
        dialog.dismiss()
    }

}