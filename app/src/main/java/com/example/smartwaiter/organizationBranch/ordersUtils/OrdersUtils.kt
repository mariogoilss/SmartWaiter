package com.example.smartwaiter.organizationBranch.ordersUtils

import android.content.Context
import android.widget.Toast
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.adapters.AdapterOrdersOrgRV
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.SaleItem
import com.example.smartwaiter.inteface.SalesList
import com.google.firebase.firestore.FirebaseFirestore

class OrdersUtils {


    companion object{
        private val db = FirebaseFirestore.getInstance()
        var ordersList = ArrayList<SalesList>()//<-- Declaramos el arrayList a devolver
        var checkerFirstLoad:Boolean = true
        private val docRef = db.collection("organizations").document(prefs.getCorreo())
        lateinit var context:Context
        lateinit var adapter: AdapterOrdersOrgRV

        var listener = docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                var list = snapshot.get("orgSalesList") as ArrayList<SalesList>
                if (!checkerFirstLoad){

                    if (list.size > ordersList.size){
                        var hash = list[list.lastIndex] as HashMap<String, String> //<-- Pillamos tabla hash BBDD
                        var objList = SalesList(
                            hash.get("date") as String,
                            hash.get("saleItemList") as ArrayList<SaleItem>,
                            hash.get("done") as Boolean,
                            hash.get("table") as Long,
                            hash.get("benefit") as Double)

                        ordersList.add(objList)
                        adapter.ordersList.add(objList)
                        adapter.notifyItemInserted(ordersList.size)
                        adapter.notifyItemRangeChanged(ordersList.size, ordersList.size)


                    }
                }

            }
        }

        

        fun stopListener(){
            listener.remove()
        }

    }
}