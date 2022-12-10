package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.*
import com.example.smartwaiter.utils.UtilsBBDD
import com.google.firebase.firestore.FirebaseFirestore

private val db = FirebaseFirestore.getInstance()

class AdapterLastShopRV : RecyclerView.Adapter<AdapterLastShopRV.ViewHolder>(){

    var shopList: ArrayList<ShopInfo> = ArrayList()
    lateinit var context: Context

    fun AdapterLastShopRV(shopList: ArrayList<ShopInfo>, context: Context) {
        this.shopList = shopList
        this.context = context
    }

    override fun onBindViewHolder(holder: AdapterLastShopRV.ViewHolder, position: Int) {
        val item = shopList.get(position)
        holder.bind(item, context, this, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterLastShopRV.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AdapterLastShopRV.ViewHolder(
            layoutInflater.inflate(R.layout.card_last_shop, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtOrganizationShop = view.findViewById(R.id.txtOrganizationShop) as TextView
        var btnSuggestionShop = view.findViewById(R.id.btnSuggestionShop) as ImageButton
        var btnTipShop = view.findViewById(R.id.btnTipShop) as ImageButton

        fun bind(shopList: ShopInfo, context: Context, adapter: AdapterLastShopRV, pos: Int ) {
            txtOrganizationShop.text = shopList.nameOrganization

            btnTipShop.setOnClickListener {
                Toast.makeText(context, "Funcion no disponible.", Toast.LENGTH_SHORT).show()
            }

            btnSuggestionShop.setOnClickListener {
                loadFragment(context, shopList.idOrganization)
            }
        }

        private fun loadFragment(context: Context, idOrganization: String){
            val builder = AlertDialog.Builder(context)

            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.dialog_suggestion,null)

            // Fragments Elements
            val txtSuggestionNote = view.findViewById<TextView>(R.id.txtSuggestionNote)!!
            val btnSendSuggestion = view.findViewById<Button>(R.id.btnSendSuggestion)!!


            // Fragment Show
            builder.setView(view)
            val dialog = builder.create() //<- se crea el dialog
            dialog.show() //<- se muestra el showdialog

            btnSendSuggestion.setOnClickListener {

                getOfBBDD(idOrganization, txtSuggestionNote)
                dialog.onBackPressed()
            }

        }


        private fun getOfBBDD(idOrganization: String, txtSuggestionNote: TextView) {
            db.collection("organizations").document(idOrganization).get().addOnSuccessListener {

                var arrayToHash = it.get("orgBankAccount") as HashMap<String, String> //<-- Pillamos tabla hash BBDD
                var bankAccount = BankAccount(
                    arrayToHash.getValue("account"),
                    arrayToHash.getValue("expirationDate"),
                    arrayToHash.getValue("secretNumber")
                )

                var organization =
                    Organization(idOrganization,
                        it.get("orgCif") as String,
                        it.get("orgFoodList") as ArrayList<MenuItem>,
                        it.get("orgDrinkList") as ArrayList<MenuItem>,
                        it.get("orgOpenOrNot") as Boolean,
                        it.get("orgSalesList") as ArrayList<SalesList>,
                        bankAccount,
                        it.get("orgSuggestionsMailBox") as ArrayList<String>,
                        it.get("orgTablesList") as ArrayList<Int>)

                organization.orgSuggestionsMailBox.add(txtSuggestionNote.text.toString())


                db.collection("organizations").document(idOrganization).set(
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



    }
}