package com.example.myapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.dataclasses.showingcartstobuy

class CarttobuyAdapters(private val userList: MutableList<showingcartstobuy>, private val onItemClick: (showingcartstobuy) -> Unit) : RecyclerView.Adapter<CarttobuyAdapters.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cartforproductstab, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentProduct = userList[position]
        holder.products.text = currentProduct.productname
        holder.productdisc.text = currentProduct.productdisc
        holder.phonenumber.text = currentProduct.phonenumber
        holder.email.text = currentProduct.email
        holder.storename.text = currentProduct.storename
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val products: TextView = itemView.findViewById(R.id.buyproname)
        val productdisc: TextView = itemView.findViewById(R.id.buyprodisc)
        val phonenumber:TextView = itemView.findViewById(R.id.catphonenumber)
        val email:TextView = itemView.findViewById(R.id.cartemail)
        val storename:TextView = itemView.findViewById(R.id.cartstorename)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(userList[position])
                }
            }
        }

    }
}