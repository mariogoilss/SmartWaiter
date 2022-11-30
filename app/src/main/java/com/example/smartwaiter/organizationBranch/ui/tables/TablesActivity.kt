package com.example.smartwaiter.organizationBranch.ui.tables

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.R
import com.example.smartwaiter.adapters.AdapterTableOrgRV
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.Organization
import com.example.smartwaiter.inteface.SalesList
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var recyclerViewTableListOrg: RecyclerView
private val adapterTableOrgRV: AdapterTableOrgRV = AdapterTableOrgRV()
var arrayTableListOrg = ArrayList<Int>()//<-- Declaramos el arrayList a devolver

class TablesActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tables)
        supportActionBar?.setTitle("Mesas disponibles")


        if (!checkPermission()){
            requestPermissions()
        }

        var btnAddTable = findViewById<ImageButton>(R.id.btnAddTable)

        btnAddTable.setOnClickListener {
            getOfBBDD(PreLoad.prefs.getCorreo())
        }

        load()


    }

    private fun checkPermission():Boolean{
        val permission1 = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        val permission2 = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
            200
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.size>0){
            val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
            val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED

            if (writeStorage && readStorage){
                Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Permisos no concedidos", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getOfBBDD(id: String) {


        db.collection("organizations").document(id).get().addOnSuccessListener {

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


            if (organization.orgTablesList.lastIndex <= 0){
                organization.orgTablesList.add(organization.orgTablesList.lastIndex + 2)
                arrayTableListOrg.add(arrayTableListOrg.lastIndex + 2)
            }else{
                organization.orgTablesList.add(organization.orgTablesList[organization.orgTablesList.lastIndex] + 1)
                arrayTableListOrg.add(arrayTableListOrg[arrayTableListOrg.lastIndex] + 1)
            }

            adapterTableOrgRV.notifyItemInserted(arrayTableListOrg.size)
            adapterTableOrgRV.notifyItemRangeChanged(arrayTableListOrg.size, arrayTableListOrg.size)


            saveOnBBDD(organization)

        }


    }


    private fun load() {
        db.collection("organizations").document(PreLoad.prefs.getCorreo()).get().addOnSuccessListener {

            arrayTableListOrg = it.get("orgTablesList") as ArrayList<Int>
            recyclerViewTableListOrg = findViewById<RecyclerView>(R.id.rvTablesOrg)
            recyclerViewTableListOrg.setHasFixedSize(true)
            recyclerViewTableListOrg.layoutManager = LinearLayoutManager(this)
            adapterTableOrgRV.AdapterTableOrgRV(arrayTableListOrg, this)
            recyclerViewTableListOrg.adapter = adapterTableOrgRV


        }

    }

    private fun saveOnBBDD(organization: Organization) {
        db.collection("organizations").document(PreLoad.prefs.getCorreo()).set(
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