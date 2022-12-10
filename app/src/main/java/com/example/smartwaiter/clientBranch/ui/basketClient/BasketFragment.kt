package com.example.smartwaiter.clientBranch.ui.basketClient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.R
import com.example.smartwaiter.adapters.AdapterBasketRV
import com.example.smartwaiter.clientBranch.basketUtils.BasketUtils


private lateinit var recyclerViewBasket: RecyclerView
private val adapterBasketRV: AdapterBasketRV = AdapterBasketRV()

class BasketFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_basket, container, false)

        var btnSendBasket = view.findViewById<Button>(R.id.btnSendBasket)


        btnSendBasket.setOnClickListener {

            BasketUtils.idOrganizations = prefs.getOrgId()
            if (BasketUtils.saleItemList.size > 0){
                BasketUtils.saveOnShopList(context!!)
                Thread.sleep(500)
                BasketUtils.getOfBBDD(adapterBasketRV, context!!)
            }else{
                Toast.makeText(context, "La cesta esta vacia", Toast.LENGTH_SHORT).show()
            }
        }

        /*
         * Cargamos el recycler
         */
        recyclerViewBasket = view.findViewById<RecyclerView>(R.id.rvBasket)
        recyclerViewBasket.setHasFixedSize(true)
        recyclerViewBasket.layoutManager = LinearLayoutManager(context!!)
        adapterBasketRV.AdapterBasketRV(BasketUtils.saleItemList, context!!)
        recyclerViewBasket.adapter = adapterBasketRV



        return view
    }


}