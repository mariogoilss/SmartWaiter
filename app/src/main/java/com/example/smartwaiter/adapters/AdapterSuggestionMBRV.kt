package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.Organization
import com.example.smartwaiter.inteface.SalesList
import com.example.smartwaiter.utils.UtilsBBDD
import com.google.firebase.firestore.FirebaseFirestore
private val db = FirebaseFirestore.getInstance()

class AdapterSuggestionMBRV : RecyclerView.Adapter<AdapterSuggestionMBRV.ViewHolder>(){


    var suggestionList: ArrayList<String> = ArrayList()
    lateinit var context: Context

    fun AdapterSuggestionMBRV(suggestionList: ArrayList<String>, context: Context) {
        this.suggestionList = suggestionList
        this.context = context
    }



    override fun onBindViewHolder(holder: AdapterSuggestionMBRV.ViewHolder, position: Int) {
        val item = suggestionList.get(position)
        holder.bind(item, context, this, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSuggestionMBRV.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AdapterSuggestionMBRV.ViewHolder(
            layoutInflater.inflate(R.layout.card_suggestion_mailbox, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return suggestionList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtSuggestionCard = view.findViewById(R.id.txtSuggestionCard) as TextView
        var btnDeleteSuggestion = view.findViewById(R.id.btnDeleteSuggestion) as ImageButton

        fun bind(suggestionList: String, context: Context, adapter: AdapterSuggestionMBRV, pos: Int) {

            if(suggestionList.length >= 20){
                var str = suggestionList.subSequence(0,15)
                txtSuggestionCard.text = " $str ..."
            }else{
                txtSuggestionCard.text = " $suggestionList"
            }

            btnDeleteSuggestion.setOnClickListener {
                loadList(adapter, pos)

            }

            itemView.setOnClickListener(View.OnClickListener {
                loadFragment(context, suggestionList)
            })
        }

        private fun loadFragment(context: Context, suggestionList: String){
            val builder = AlertDialog.Builder(context)

            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.dialog_suggestion,null)

            // Fragments Elements
            val txtSuggestionNote = view.findViewById<TextView>(R.id.txtSuggestionNote)!!
            val btnSendSuggestion = view.findViewById<Button>(R.id.btnSendSuggestion)!!
            txtSuggestionNote.setText(suggestionList)
            txtSuggestionNote.isFocusable = false

            btnSendSuggestion.isVisible = false


            // Fragment Show
            builder.setView(view)
            val dialog = builder.create() //<- se crea el dialog
            dialog.show() //<- se muestra el showdialog

        }

        private fun loadList(adapter: AdapterSuggestionMBRV, pos: Int) {
            db.collection("organizations").document(PreLoad.prefs.getCorreo()).get().addOnSuccessListener {

                var arrayToHash = it.get("orgBankAccount") as HashMap<String, String> //<-- Pillamos tabla hash BBDD
                var bankAccount = BankAccount(
                    arrayToHash.getValue("account"),
                    arrayToHash.getValue("expirationDate"),
                    arrayToHash.getValue("secretNumber")
                )

                var organzation =
                    Organization(it.get("orgName") as String,
                        it.get("orgCif") as String,
                        it.get("orgFoodList") as ArrayList<MenuItem>,
                        it.get("orgDrinkList") as ArrayList<MenuItem>,
                        it.get("orgOpenOrNot") as Boolean,
                        it.get("orgSalesList") as ArrayList<SalesList>,
                        bankAccount,
                        it.get("orgSuggestionsMailBox") as ArrayList<String>,
                        it.get("orgTablesList") as ArrayList<Int>)

                adapter.suggestionList.removeAt(pos)
                adapter.notifyItemRemoved(pos)
                adapter.notifyItemRangeChanged(0, adapter.suggestionList.size)

                organzation.orgSuggestionsMailBox = adapter.suggestionList

                UtilsBBDD.saveOnBBDD(organzation)
            }
        }

    }
}