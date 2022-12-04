package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.*



class AdapterBasketRV : RecyclerView.Adapter<AdapterBasketRV.ViewHolder>(){

    var saleItemList: ArrayList<SaleItem> = ArrayList()
    lateinit var context: Context

    fun AdapterBasketRV(saleItemList: ArrayList<SaleItem>, context: Context) {
        this.saleItemList = saleItemList
        this.context = context
    }

    override fun onBindViewHolder(holder: AdapterBasketRV.ViewHolder, position: Int) {
        val item = saleItemList.get(position)
        holder.bind(item, context, this, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterBasketRV.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AdapterBasketRV.ViewHolder(
            layoutInflater.inflate(R.layout.card_basket_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return saleItemList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bind(saleItemList: SaleItem, context: Context, adapter: AdapterBasketRV, pos: Int, ) {

        }


    }













}