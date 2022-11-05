package com.example.smartwaiter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.organizationBranch.MainOrganization
import com.example.smartwaiter.registerType.RegisterClientActivity
import com.example.smartwaiter.registerType.RegisterOwnerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.lang.Exception


private var GOOGLE_SIGN_IN = 1

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val register = findViewById<TextView>(R.id.txtRegister)!!
        val login = findViewById<Button>(R.id.btnLogin)
        val email = findViewById<EditText>(R.id.txtLogEmail)
        val password = findViewById<EditText>(R.id.txtLogPwd)
        val remember = findViewById<CheckBox>(R.id.cbxRemember)

        register.setOnClickListener {
            loadForm()
        }

        login.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        mainEntry(email.text.toString())
                    }else{
                        Toast.makeText(this, "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    /*
     * Control de la accion de logearse
     * @btnRegister
     */
    fun mainEntry(email:String){
        if (email != null) {
            db.collection("organizations").document(email).get().addOnSuccessListener {
                if(it.exists()){
                    val intent = Intent(this, MainOrganization::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Sin terminar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    /*
     * Control de la accion de registrarse
     * @txtRegister
     */
    private fun loadForm(){
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_register_options,null)
        val owner = view.findViewById<TextView>(R.id.btnOwner)!!
        val client = view.findViewById<TextView>(R.id.btnClient)!!
        builder.setView(view)

        val dialog = builder.create() //<- se crea el dialog
        dialog.show() //<- se muestra el showdialog

        owner.setOnClickListener {
            formRegisterOwner()
        }

        client.setOnClickListener {
            formRegisterClient()
        }
    }

    private fun formRegisterOwner(){
        val intentRegister = Intent(this, RegisterOwnerActivity::class.java)
        startActivity(intentRegister)
    }

    private fun formRegisterClient(){
        val intentRegister = Intent(this, RegisterClientActivity::class.java)
        startActivity(intentRegister)
    }

    override fun onBackPressed() {
        finishAffinity()
    }


}