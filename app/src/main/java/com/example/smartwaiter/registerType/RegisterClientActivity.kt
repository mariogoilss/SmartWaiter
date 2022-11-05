package com.example.smartwaiter.registerType

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smartwaiter.MainActivity
import com.example.smartwaiter.R
import com.google.firebase.auth.FirebaseAuth

class RegisterClientActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_client)
        supportActionBar?.hide()
        val btn = findViewById<Button>(R.id.btnSend)
        val email = findViewById<EditText>(R.id.txtEmail)
        val pwd = findViewById<EditText>(R.id.txtPwd)



        btn.setOnClickListener {

            if (email.text.isNotEmpty() && pwd.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(),pwd.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
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



}