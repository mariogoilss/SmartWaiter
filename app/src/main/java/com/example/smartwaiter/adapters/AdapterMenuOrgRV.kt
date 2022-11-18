package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.MenuItem

class AdapterMenuOrgRV : RecyclerView.Adapter<AdapterMenuOrgRV.ViewHolder>(){
    var itemMenuList: ArrayList<MenuItem> = ArrayList()
    lateinit var context: Context

    fun AdapterMenuOrgRV(itemMenuList: ArrayList<MenuItem>, context: Context) {
        this.itemMenuList = itemMenuList
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemMenuList.get(position)
        holder.bind(item, context, this, itemMenuList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.card_menu_org,parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemMenuList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(itemMenu: MenuItem, context: Context, adapter: AdapterMenuOrgRV, correo: ArrayList<MenuItem>) {


            itemView.setOnClickListener(View.OnClickListener {

            })

        }


    }



}
