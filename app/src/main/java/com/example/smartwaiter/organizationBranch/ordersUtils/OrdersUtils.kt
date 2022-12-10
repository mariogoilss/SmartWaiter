package com.example.smartwaiter.organizationBranch.ordersUtils

import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.adapters.AdapterOrdersOrgRV
import com.example.smartwaiter.inteface.SalesList

class OrdersUtils {
    private lateinit var recyclerViewOrders: RecyclerView
    private val adapterOrdersOrgRV: AdapterOrdersOrgRV = AdapterOrdersOrgRV()

    companion object{

        var ordersList = ArrayList<SalesList>()//<-- Declaramos el arrayList a devolver
        var checker = true
        var timer:Int = 0
        var stop = true


    }
}