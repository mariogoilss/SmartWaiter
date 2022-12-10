package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.R


class AdapterSuggestionMBRV : RecyclerView.Adapter<AdapterSuggestionMBRV.ViewHolder>(){


    var suggestionList: ArrayList<String> = ArrayList()
    lateinit var context: Context

    fun AdapterSuggestionMBRV(suggestionList: ArrayList<String>, context: Context) {
        this.suggestionList = suggestionList
        this.context = context
    }



    override fun onBindViewHolder(holder: AdapterSuggestionMBRV.ViewHolder, position: Int) {
        val item = suggestionList.get(position)
        holder.bind(item, context, this, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSuggestionMBRV.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AdapterSuggestionMBRV.ViewHolder(
            layoutInflater.inflate(R.layout.card_order_detail, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return suggestionList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bind(suggestionList: String, context: Context, adapter: AdapterSuggestionMBRV, pos: Int) {

        }


    }
}