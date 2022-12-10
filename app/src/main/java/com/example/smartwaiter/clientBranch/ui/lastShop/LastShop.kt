package com.example.smartwaiter.clientBranch.ui.lastShop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.R
import com.example.smartwaiter.adapters.AdapterLastShopRV
import com.example.smartwaiter.clientBranch.basketUtils.BasketUtils
import com.example.smartwaiter.inteface.*
import com.google.firebase.firestore.FirebaseFirestore

class LastShop : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    var lastShop = ArrayList<ShopInfo>()
    private lateinit var recyclerViewLastShop: RecyclerView
    private val adapterLastShop: AdapterLastShopRV = AdapterLastShopRV()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_shop)
        supportActionBar?.setTitle("Establecimientos visitados")

        load()
    }

    private fun load(){
        db.collection("users").document(PreLoad.prefs.getCorreo()).get().addOnSuccessListener {


            var arrayToHash =
                it.get("usrBankAccount") as HashMap<String, String> //<-- Pillamos tabla hash BBDD

            var bankAccount = BankAccount(
                arrayToHash.getValue("account"),
                arrayToHash.getValue("expirationDate"),
                arrayToHash.getValue("secretNumber")
            )
            var client = Client(
                it.get("usrName") as String,
                it.get("usrImageProfile") as String,
                bankAccount,
                it.get("usrShopList") as ArrayList<ShopInfo>
            )


            for (index in 0 until client.shopList.size) {
                var hash = client.shopList[index] as HashMap<String, String>

                var objShop = ShopInfo(hash.getValue("idOrganization"), hash.getValue("nameOrganization"))
                lastShop.add(objShop)

            }

            recyclerViewLastShop = findViewById<RecyclerView>(R.id.rvLastShop)
            recyclerViewLastShop.setHasFixedSize(true)
            recyclerViewLastShop.layoutManager = LinearLayoutManager(this)
            adapterLastShop.AdapterLastShopRV(lastShop, this)
            recyclerViewLastShop.adapter = adapterLastShop
        }
    }
}