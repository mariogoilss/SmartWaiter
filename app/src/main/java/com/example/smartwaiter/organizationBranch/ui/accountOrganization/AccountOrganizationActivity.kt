package com.example.smartwaiter.organizationBranch.ui.accountOrganization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.*
import com.example.smartwaiter.utils.UtilsBBDD
import com.google.firebase.firestore.FirebaseFirestore

class AccountOrganizationActivity : AppCompatActivity() {
    private lateinit var organization: Organization
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_organization)

        var txtCifAccount = findViewById<EditText>(R.id.txtCifAccountOrg)!!
        var txtNameAccount = findViewById<EditText>(R.id.txtNameAccountOrg)!!
        var txtBankAccount = findViewById<EditText>(R.id.txtBankAccountOrg)!!
        var txtDayAccount = findViewById<EditText>(R.id.txtDayAccountOrg)!!
        var txtMonthAccount = findViewById<EditText>(R.id.txtMonthAccountOrg)!!
        var txtCvvAccount = findViewById<EditText>(R.id.txtCvvAccountOrg)!!
        var btnSendAccount = findViewById<Button>(R.id.btnSendAccountOrg)!!

        txtCifAccount.isFocusable = false
        txtNameAccount.isFocusable = false

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

            val list: List<String> = listOf(*organization.orgBankAccount.expirationDate.split("/").toTypedArray())

            txtCifAccount.setText(organization.orgCif)
            txtNameAccount.setText(organization.orgName)
            txtBankAccount.setText(organization.orgBankAccount.account)
            txtDayAccount.setText(list[0])
            txtMonthAccount.setText(list[1])
            txtCvvAccount.setText(organization.orgBankAccount.secretNumber)

        }

        btnSendAccount.setOnClickListener {
            try {
                if(txtBankAccount.text.isNotEmpty() && txtDayAccount.text.isNotEmpty() && txtMonthAccount.text.isNotEmpty() &&  txtCvvAccount.text.isNotEmpty()){
                    var bank = BankAccount(txtBankAccount.text.toString(), ("" + txtDayAccount.text.toString() + "/" + txtMonthAccount.text.toString()), txtCvvAccount.text.toString() )
                    organization.orgBankAccount = bank
                    UtilsBBDD.saveOnBBDD(organization)
                    Toast.makeText(this, "Cambios realizados correctamente.", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }else{
                    Toast.makeText(this, "Los campos no pueden estar vacios o son incorrectos.", Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                Toast.makeText(this, "Los campos no pueden estar vacios o son incorrectos.", Toast.LENGTH_SHORT).show()
            }
        }


    }
}