package com.example.smartwaiter.organizationBranch.ui.orders

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.R
import com.example.smartwaiter.adapters.AdapterOrdersOrgRV
import com.example.smartwaiter.inteface.*
import com.example.smartwaiter.organizationBranch.MainOrganizationNav
import com.google.firebase.firestore.FirebaseFirestore


private lateinit var recyclerViewOrders: RecyclerView
private val adapterOrdersOrgRV: AdapterOrdersOrgRV = AdapterOrdersOrgRV()
var ordersList = ArrayList<SalesList>()//<-- Declaramos el arrayList a devolver


class OrdersFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_orders, container, false)

        if (!prefs.getOpenOrNot()){
            if (mostrar_emergente()){
                val intent = Intent(context, MainOrganizationNav::class.java)
                startActivity(intent)
            }

        }else{
            if (ordersList.isEmpty()) {
                load(view)
            } else {
                reloadRecycler(view)
            }
        }

        return view
    }

    fun reloadRecycler(view: View){
        recyclerViewOrders = view.findViewById<RecyclerView>(R.id.rvOrdersOrg)
        recyclerViewOrders.setHasFixedSize(true)
        recyclerViewOrders.layoutManager = LinearLayoutManager(context!!)
        adapterOrdersOrgRV.AdapterOrdersOrgRV(ordersList, context!!)
        recyclerViewOrders.adapter = adapterOrdersOrgRV
    }

    fun mostrar_emergente():Boolean{
        var checker = false
        val builder = AlertDialog.Builder(context)
        builder.setTitle("El establecimiento es cerrado.")
        builder.setMessage("¿Desea abrirlo?")
        builder.setPositiveButton("Si",{ dialogInterface: DialogInterface, i: Int ->
            openOrganization()
            checker = true})
        builder.setNegativeButton("No",{ dialogInterface: DialogInterface, i: Int -> })
        builder.show()

        return checker

    }

    private fun openOrganization() {
        prefs.saveOpenOrNot(true)
        getOfBBDD(prefs.getOpenOrNot())
    }

    private fun closeOrganization() {
        prefs.saveOpenOrNot(false)
        getOfBBDD(prefs.getOpenOrNot())
    }




    private fun load(view: View) {
        db.collection("organizations").document(prefs.getCorreo()).get().addOnSuccessListener {

            if(it.get("orgSalesList") != null){
                var arrayToHash = ArrayList<SalesList>()
                arrayToHash =
                    it.get("orgSalesList") as ArrayList<SalesList> //<-- Pillamos tabla hash BBDD


                for (i in 0 until arrayToHash.size) {
                    val x = arrayToHash[i] as HashMap<String, String>

                    var salesList = SalesList(
                        x.getValue("date"),
                        x.getValue("saleItemList") as ArrayList<SaleItem>,
                        x.getValue("done") as Boolean,
                        x.getValue("table") as Long,
                        x.getValue("benefit") as Double
                    )

                    ordersList.add(salesList)
                }

                reloadRecycler(view)
            }
        }

    }

    private fun getOfBBDD(openOrNot:Boolean){
        db.collection("organizations").document(prefs.getCorreo()).get().addOnSuccessListener {

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

            organization.orgOpenOrNot = openOrNot
            saveOnBBDD(organization)
        }
    }

    private fun saveOnBBDD(organization: Organization){
        db.collection("organizations").document(prefs.getCorreo()).set(
            hashMapOf(
                "orgName" to organization.orgName,
                "orgCif" to organization.orgCif,
                "orgFoodList" to organization.orgFoodList,
                "orgDrinkList" to organization.orgDrinkList,
                "orgOpenOrNot" to organization.orgOpenOrNot,
                "orgSalesList" to organization.orgSalesList,
                "orgBankAccount" to organization.orgBankAccount,
                "orgSuggestionsMailBox" to organization.orgSuggestionsMailBox,
                "orgTablesList" to organization.orgTablesList
            )
        )
    }



}