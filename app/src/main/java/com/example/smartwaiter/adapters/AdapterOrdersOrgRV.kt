package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.SalesList

class AdapterOrdersOrgRV : RecyclerView.Adapter<AdapterOrdersOrgRV.ViewHolder>(){

    var ordersList: ArrayList<SalesList> = ArrayList()
    lateinit var context: Context

    fun AdapterOrdersOrgRV(ordersList: ArrayList<SalesList>, context: Context) {
        this.ordersList = ordersList
        this.context = context
    }

    override fun onBindViewHolder(holder: AdapterOrdersOrgRV.ViewHolder, position: Int) {
        val item = ordersList.get(position)
        holder.bind(item, context, this, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterOrdersOrgRV.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AdapterOrdersOrgRV.ViewHolder(
            layoutInflater.inflate(R.layout.card_orders_organization, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(ordersList: SalesList, context: Context, adapter: AdapterOrdersOrgRV, pos: Int ) {

        }

    }
}