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
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.organizationBranch.MainOrganizationNav
import com.example.smartwaiter.registerType.RegisterClientActivity
import com.example.smartwaiter.registerType.RegisterOwnerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


private var GOOGLE_SIGN_IN = 1

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        if (prefs.getRecor()) { //<-- Si tiene todos los datos guardados entra directamente
            memoryEntry()

        }else{
            setContentView(R.layout.activity_main)
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
                            saveInMemory(email.text.toString(), remember)
                            mainEntry(email.text.toString())
                        }else{
                            Toast.makeText(this, "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    /*
     * Control de la memoria
     * @btnRegister
     */
    fun saveInMemory(email:String, remember: CheckBox){
        prefs.saveCorreo(email)
        prefs.saveRecordar(remember.isChecked)
    }

    fun memoryEntry(){
        db.collection("organizations").document(prefs.getCorreo()).get().addOnSuccessListener {
            if(it.exists()){
                val intent = Intent(this, MainOrganizationNav::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Sin terminar", Toast.LENGTH_SHORT).show()
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
                    val intent = Intent(this, MainOrganizationNav::class.java)
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