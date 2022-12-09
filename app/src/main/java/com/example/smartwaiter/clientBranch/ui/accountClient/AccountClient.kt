package com.example.smartwaiter.clientBranch.ui.accountClient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.Client
import com.example.smartwaiter.inteface.ShopInfo
import com.example.smartwaiter.utils.UtilsBBDD
import com.google.firebase.firestore.FirebaseFirestore

class AccountClient : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var client: Client
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_client)

        var txtNameAccount = findViewById<EditText>(R.id.txtNameAccount)!!
        var txtBankAccount = findViewById<EditText>(R.id.txtBankAccount)!!
        var txtDayAccount = findViewById<EditText>(R.id.txtDayAccount)!!
        var txtMonthAccount = findViewById<EditText>(R.id.txtMonthAccount)!!
        var txtCvvAccount = findViewById<EditText>(R.id.txtCvvAccount)!!
        var btnSendAccount = findViewById<Button>(R.id.btnSendAccount)!!

        db.collection("users").document(PreLoad.prefs.getCorreo()).get().addOnSuccessListener {
            var arrayToHash = it.get("usrBankAccount") as HashMap<String, String> //<-- Pillamos tabla hash BBDD
            var bankAccount = BankAccount(
                arrayToHash.getValue("account"),
                arrayToHash.getValue("expirationDate"),
                arrayToHash.getValue("secretNumber")
            )

            var clt = Client(it.get("usrName") as String, it.get("usrImageProfile") as String, bankAccount, it.get("usrShopList") as ArrayList<ShopInfo>)
            client = clt


            val list: List<String> = listOf(*client.bankAccount.expirationDate.split("/").toTypedArray())

            txtNameAccount.setText(client.name)
            txtBankAccount.setText(client.bankAccount.account)
            txtDayAccount.setText(list[0])
            txtMonthAccount.setText(list[1])
            txtCvvAccount.setText(client.bankAccount.secretNumber)

        }

        btnSendAccount.setOnClickListener {
            
            try {
                if(txtNameAccount.text.isNotEmpty() && txtBankAccount.text.isNotEmpty() && txtDayAccount.text.isNotEmpty() && txtMonthAccount.text.isNotEmpty() &&  txtCvvAccount.text.isNotEmpty()){
                    var bank = BankAccount(txtBankAccount.text.toString(), ("" + txtDayAccount.text.toString() + "/" + txtMonthAccount.text.toString()), txtCvvAccount.text.toString() )
                    client.name = txtNameAccount.text.toString()
                    client.bankAccount = bank
                    UtilsBBDD.saveOnBBDDClient(client)
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