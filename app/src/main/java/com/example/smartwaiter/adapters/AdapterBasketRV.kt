package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        var name = view.findViewById(R.id.txtNameBasket) as TextView
        var price = view.findViewById(R.id.txtPriceBasket) as TextView
        var amount = view.findViewById(R.id.txtAmountBasket) as TextView
        var image = view.findViewById(R.id.imgBasketItem) as ImageView
        var btnMinus = view.findViewById(R.id.btnMinusBasket) as TextView
        var btnPlus = view.findViewById(R.id.btnPlusBasket) as TextView

        fun bind(saleItemList: SaleItem, context: Context, adapter: AdapterBasketRV, pos: Int, ) {
            name.text = saleItemList.menuItem.name.toString()
            price.text = saleItemList.menuItem.price.toString()
            amount.text = saleItemList.amount.toString()
            image.setImageResource(R.drawable.comida);

            btnMinus.setOnClickListener {

                saleItemList.amount = saleItemList.amount - 1
                if (saleItemList.amount > 0){
                    amount.text = saleItemList.amount.toString()
                }else{
                    adapter.saleItemList.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                    adapter.notifyItemRangeChanged( 0, adapter.saleItemList.size)
                }
            }

            btnPlus.setOnClickListener {
                saleItemList.amount = saleItemList.amount + 1
                amount.text = saleItemList.amount.toString()
            }

        }

    }













}