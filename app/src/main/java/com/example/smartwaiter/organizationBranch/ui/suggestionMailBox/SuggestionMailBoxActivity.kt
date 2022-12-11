package com.example.smartwaiter.organizationBranch.ui.suggestionMailBox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.R
import com.example.smartwaiter.adapters.AdapterSuggestionMBRV
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.Organization
import com.example.smartwaiter.inteface.SalesList

import com.google.firebase.firestore.FirebaseFirestore

class SuggestionMailBoxActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var organization: Organization
    private lateinit var recyclerMailBox: RecyclerView
    private val adapterSuggestionMBRV: AdapterSuggestionMBRV = AdapterSuggestionMBRV()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion_mail_box)

        loadList()

    }

    private fun loadList() {
        db.collection("organizations").document(PreLoad.prefs.getCorreo()).get().addOnSuccessListener {

            var arrayToHash = it.get("orgBankAccount") as HashMap<String, String> //<-- Pillamos tabla hash BBDD
            var bankAccount = BankAccount(
                arrayToHash.getValue("account"),
                arrayToHash.getValue("expirationDate"),
                arrayToHash.getValue("secretNumber")
            )

            var org =
                Organization(it.get("orgName") as String,
                    it.get("orgCif") as String,
                    it.get("orgFoodList") as ArrayList<MenuItem>,
                    it.get("orgDrinkList") as ArrayList<MenuItem>,
                    it.get("orgOpenOrNot") as Boolean,
                    it.get("orgSalesList") as ArrayList<SalesList>,
                    bankAccount,
                    it.get("orgSuggestionsMailBox") as ArrayList<String>,
                    it.get("orgTablesList") as ArrayList<Int>)


            organization = org

            recyclerMailBox = findViewById<RecyclerView>(R.id.rvSuggestionMailBox)
            recyclerMailBox.setHasFixedSize(true)
            recyclerMailBox.layoutManager = LinearLayoutManager(this)
            adapterSuggestionMBRV.AdapterSuggestionMBRV(organization.orgSuggestionsMailBox, this)
            recyclerMailBox.adapter = adapterSuggestionMBRV


        }
    }
}