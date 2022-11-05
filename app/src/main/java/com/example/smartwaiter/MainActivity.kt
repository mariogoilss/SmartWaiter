package com.example.smartwaiter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.smartwaiter.registerType.RegisterClientActivity
import com.example.smartwaiter.registerType.RegisterOwnerActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val register = findViewById<TextView>(R.id.txtRegister)!!

        register.setOnClickListener {
            loadForm()
        }
    }

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