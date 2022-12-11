package com.example.smartwaiter.organizationBranch.ui.orderDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.R
import com.example.smartwaiter.adapters.AdapterOrderDetailRV
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.SaleItem
import com.example.smartwaiter.organizationBranch.ordersUtils.OrdersUtils


private lateinit var recyclerViewDetail: RecyclerView
private val adapterOrderDetailRV: AdapterOrderDetailRV = AdapterOrderDetailRV()
var listItem = ArrayList<SaleItem>()

class OrderDatailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_datail)

        var bundle = intent.extras
        var obj = bundle?.getInt("position")


        var btnBackDetail = findViewById<Button>(R.id.btnBackDetail)
        btnBackDetail.setOnClickListener {
            onBackPressed()
        }

        listItem.clear()


        for (i in 0 until OrdersUtils.ordersList[obj!!].saleItemList.size){
            var hash = OrdersUtils.ordersList[obj!!].saleItemList[i] as HashMap<String, String> //<-- Pillamos tabla hash BBDD

            var hassMenuItem = hash.get("menuItem")  as HashMap<String, String>
            var menuItem = MenuItem(
                hassMenuItem.get("name") as String,
                hassMenuItem.get("description") as String,
                hassMenuItem.get("price") as Double,
                hassMenuItem.get("allergens") as ArrayList<Int>,
                hassMenuItem.get("image") as String,
                (hassMenuItem.get("amountStock") as Long).toInt())


            var objList = SaleItem(
                menuItem,
                (hash.get("amount") as Long).toInt(),
                0.0)

            listItem.add(objList)
        }

        recyclerViewDetail = findViewById<RecyclerView>(R.id.rvOrderDetail)
        recyclerViewDetail.setHasFixedSize(true)
        recyclerViewDetail.layoutManager = LinearLayoutManager(this)
        adapterOrderDetailRV.AdapterOrderDetailRV(listItem, this)
        recyclerViewDetail.adapter = adapterOrderDetailRV



    }
}