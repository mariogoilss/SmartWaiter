package com.example.smartwaiter.organizationBranch.ui.orders

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.R
import com.example.smartwaiter.adapters.AdapterOrdersOrgRV
import com.example.smartwaiter.inteface.*
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.organizationBranch.MainOrganizationNav
import com.google.firebase.auth.FirebaseAuth
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
        recyclerViewOrders = view.findViewById<RecyclerView>(R.id.rvOrdersOrg)
        if (!prefs.getOpenOrNot()){
            mostrar_emergente()
        }else{
            if (ordersList.isEmpty()) {
                load()
            } else {
                reloadRecycler()
            }
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = inflater
        inflater.inflate(R.menu.open_or_close_menu, menu)
    }

    fun reloadRecycler(){
        recyclerViewOrders.setHasFixedSize(true)
        recyclerViewOrders.layoutManager = LinearLayoutManager(context!!)
        adapterOrdersOrgRV.AdapterOrdersOrgRV(ordersList, context!!)
        recyclerViewOrders.adapter = adapterOrdersOrgRV
    }

    fun mostrar_emergente(){

        val builder = AlertDialog.Builder(context)
        builder.setTitle("El establecimiento es cerrado.")
        builder.setMessage("¿Desea abrirlo?")
        builder.setPositiveButton("Si",{ dialogInterface: DialogInterface, i: Int ->
            openOrganization()
            load()
            reloadRecycler()
        })
        builder.setNegativeButton("No",{ dialogInterface: DialogInterface, i: Int -> })
        builder.show()


    }

    private fun openOrganization() {
        prefs.saveOpenOrNot(true)
        getOfBBDD(prefs.getOpenOrNot())
    }

    private fun closeOrganization() {
        prefs.saveOpenOrNot(false)
        getOfBBDD(prefs.getOpenOrNot())
    }

    private fun load() {
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

                reloadRecycler()
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


    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when(item.itemId){
            R.id.btnOpenOrganization -> {
                if (!prefs.getOpenOrNot()){
                    openOrganization()
                    load()
                    reloadRecycler()
                    Toast.makeText(context, "Establecimiento abierto", Toast.LENGTH_SHORT).show()
                }else{
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Atencion")
                    builder.setMessage("El establecimiento ya esta abierto.")
                    builder.setPositiveButton("Aceptar",{ dialogInterface: DialogInterface, i: Int -> })
                    builder.show()
                }

            }

            R.id.btnCloseOrganization -> {
                if(prefs.getOpenOrNot()){
                    closeOrganization()
                    ordersList.clear()
                    reloadRecycler()
                    Toast.makeText(context, "Establecimiento cerrado", Toast.LENGTH_SHORT).show()


                }else{
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Atencion")
                    builder.setMessage("El establecimiento ya esta cerrado.")
                    builder.setPositiveButton("Aceptar",{ dialogInterface: DialogInterface, i: Int -> })
                    builder.show()

                }
            }

        }
        return super.onOptionsItemSelected(item)
    }




}