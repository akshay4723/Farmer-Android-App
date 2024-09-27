package com.example.myapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.products.Searchproduct

class ProductAdapters(private val userList: MutableList<Searchproduct>, private val onItemClick: (Searchproduct) -> Unit) : RecyclerView.Adapter<ProductAdapters.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.searchedproducttabs, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentProduct = userList[position]
        holder.products.text = currentProduct.productname
        holder.productdisc.text = currentProduct.productdisc

    }


    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val products: TextView = itemView.findViewById(R.id.buyproname)
        val productdisc: TextView = itemView.findViewById(R.id.buyprodisc)

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