package com.example.smartwaiter.registerType

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smartwaiter.MainActivity
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.SaleItem
import com.example.smartwaiter.inteface.SalesList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterOwnerActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_owner)
        supportActionBar?.hide()
        val btn = findViewById<Button>(R.id.btnSend)
        val email = findViewById<EditText>(R.id.txtEmail)
        val pwd = findViewById<EditText>(R.id.txtPwd)
        val name = findViewById<EditText>(R.id.txtName)
        val cif = findViewById<EditText>(R.id.txtCif)


        btn.setOnClickListener {

            if (email.text.isNotEmpty() && pwd.text.isNotEmpty() && name.text.isNotEmpty() && cif.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(),pwd.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        saveInBBDD(name.text.toString(),email.text.toString(),cif.text.toString())
                        Toast.makeText(this, "Registro completo, ya puede disfrutar de la aplicacion", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error el correo o la contraseña no es valida", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Los campos del formulario no pueden estar vacios", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun saveInBBDD(name:String, email:String, cif:String){

        db.collection("organizations").document(email).set(
            hashMapOf(
                "orgName" to name,
                "orgCif" to cif,
                "orgFoodList" to arrayListOf<MenuItem>(),
                "orgDrinkList" to arrayListOf<MenuItem>(),
                "orgOpenOrNot" to false,
                "orgSalesList" to arrayListOf<SalesList>(),
                "orgBankAccount" to BankAccount("123456789","21/7","123"),
                "orgSuggestionsMailBox" to arrayListOf<String>(),
                "orgTablesList" to arrayListOf<String>()
                )
        )
    }
}