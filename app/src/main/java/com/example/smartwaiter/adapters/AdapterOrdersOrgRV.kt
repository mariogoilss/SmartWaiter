package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.Organization
import com.example.smartwaiter.inteface.SalesList
import com.example.smartwaiter.utils.UtilsBBDD
import com.google.firebase.firestore.FirebaseFirestore

private val db = FirebaseFirestore.getInstance()

class AdapterOrdersOrgRV : RecyclerView.Adapter<AdapterOrdersOrgRV.ViewHolder>(){

    var ordersList: ArrayList<SalesList> = ArrayList()
    lateinit var context: Context

    fun AdapterOrdersOrgRV(ordersList: ArrayList<SalesList>, context: Context) {
        this.ordersList = onlyFalses(ordersList)
        this.context = context
    }

    fun onlyFalses(ordersList: ArrayList<SalesList>): ArrayList<SalesList>{
        var list = ArrayList<SalesList>()
        for (i in 0 until ordersList.size){
            if (!ordersList[i].done){
                list.add(ordersList[i])
            }
        }
        return list
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

        val txtTableOrder = view.findViewById<TextView>(R.id.txtTableOrder)!!
        val txtDateOrder = view.findViewById<TextView>(R.id.txtDateOrder)!!
        val btnCheckOrder = view.findViewById<ImageButton>(R.id.btnCheckOrder)!!

        fun bind(ordersList: SalesList, context: Context, adapter: AdapterOrdersOrgRV, pos: Int ) {
            txtTableOrder.text = ordersList.table.toString()
            txtDateOrder.text = ordersList.date.toString()

            btnCheckOrder.setOnClickListener {

            }
        }

        private fun getOfBBDD(openOrNot:Boolean){
            db.collection("organizations").document(PreLoad.prefs.getCorreo()).get().addOnSuccessListener {

                var arrayToHash = it.get("orgBankAccount") as HashMap<String, String> //<-- Pillamos tabla hash BBDD
                var bankAccount = BankAccount(
                    arrayToHash.getValue("account"),
                    arrayToHash.getValue("expirationDate"),
                    arrayToHash.getValue("secretNumber")
                )

                var organization =
                    Organization(it.get("orgName") as String,
                        it.get("orgCif") as String,
                        it.get("orgFoodList") as ArrayList<MenuItem>,
                        it.get("orgDrinkList") as ArrayList<MenuItem>,
                        it.get("orgOpenOrNot") as Boolean,
                        it.get("orgSalesList") as ArrayList<SalesList>,
                        bankAccount,
                        it.get("orgSuggestionsMailBox") as ArrayList<String>,
                        it.get("orgTablesList") as ArrayList<Int>)

                UtilsBBDD.saveOnBBDD(organization)
            }

        }



    }
}