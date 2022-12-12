package com.example.smartwaiter.clientBranch

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smartwaiter.R
import com.example.smartwaiter.databinding.ActivityMainClientNavBinding

class MainClientActivityNav : AppCompatActivity() {

    private lateinit var binding: ActivityMainClientNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainClientNavBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main_client)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_menuClient, R.id.navigation_basket ,R.id.navigation_managementClient
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        mostrar_emergente()
    }

    fun mostrar_emergente(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta")
        builder.setMessage("¿Desea salir de la aplicacion?")
        builder.setPositiveButton("Si",{ dialogInterface: DialogInterface, i: Int ->
            finishAffinity()
        })

        builder.setNegativeButton("No",{ dialogInterface: DialogInterface, i: Int ->
        })
        builder.show()
    }
}