package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.SaleItem



class AdapterOrderDetailRV : RecyclerView.Adapter<AdapterOrderDetailRV.ViewHolder>(){


    var saleItemList: ArrayList<SaleItem> = ArrayList()
    lateinit var context: Context

    fun AdapterOrderDetailRV(saleItemList: ArrayList<SaleItem>, context: Context) {
        this.saleItemList = saleItemList
        this.context = context
    }



    override fun onBindViewHolder(holder: AdapterOrderDetailRV.ViewHolder, position: Int) {
        val item = saleItemList.get(position)
        holder.bind(item, context, this, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterOrderDetailRV.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AdapterOrderDetailRV.ViewHolder(
            layoutInflater.inflate(R.layout.card_order_detail, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return saleItemList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtDetailName = view.findViewById(R.id.txtDetailName) as TextView
        var txtDetailAmount = view.findViewById(R.id.txtDetailAmount) as TextView



        fun bind(saleItemList: SaleItem, context: Context, adapter: AdapterOrderDetailRV, pos: Int) {
            txtDetailName.text = saleItemList.menuItem.name
            txtDetailAmount.text = saleItemList.amount.toString()
        }


    }
}