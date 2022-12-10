package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.*
import com.example.smartwaiter.utils.UtilsBBDD
import com.google.firebase.firestore.FirebaseFirestore

private val db = FirebaseFirestore.getInstance()

class AdapterOrdersOrgRV : RecyclerView.Adapter<AdapterOrdersOrgRV.ViewHolder>(){


    var ordersList: ArrayList<SalesList> = ArrayList()
    var originalList: ArrayList<SalesList> = ArrayList()
    lateinit var context: Context

    fun AdapterOrdersOrgRV(ordersList: ArrayList<SalesList>, context: Context) {
        this.originalList = ordersList
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
        holder.bind(item, context, this, position, originalList)
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
        val btnViewOrder = view.findViewById<ImageButton>(R.id.btnViewOrder)!!

        fun bind(ordersList: SalesList, context: Context, adapter: AdapterOrdersOrgRV, pos: Int, originalList:ArrayList<SalesList>) {
            txtTableOrder.text = ordersList.table.toString()
            txtDateOrder.text = ordersList.date.toString()

            btnCheckOrder.setOnClickListener {
                getOfBBDD(pos,context ,adapter, originalList,ordersList)
            }

            btnViewOrder.setOnClickListener {
                Toast.makeText(context, "" +pos, Toast.LENGTH_SHORT).show()
            }
        }

        private fun getOfBBDD(
            pos: Int,
            context: Context,
            adapter: AdapterOrdersOrgRV,
            originalList: ArrayList<SalesList>,
            objSale: SalesList
        ) {



            db.collection("organizations").document(PreLoad.prefs.getCorreo()).get().addOnSuccessListener {

                var position = 0
                for (i in 0 until originalList.size){
                    //Toast.makeText(context, "posrv -> ${pos}, posbbdd -> $i", Toast.LENGTH_SHORT).show()
                    if (originalList[i] == objSale){
                        originalList[i].done = true
                    }
                }

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


                organization.orgSalesList = originalList
                UtilsBBDD.saveOnBBDD(organization)


                adapter.ordersList.removeAt(pos)
                adapter.notifyItemRemoved(pos)
                adapter.notifyItemRangeChanged( 0, adapter.ordersList.size)

            }

        }



    }
}