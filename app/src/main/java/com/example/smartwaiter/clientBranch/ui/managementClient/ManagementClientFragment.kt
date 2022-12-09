package com.example.smartwaiter.clientBranch.ui.managementClient

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.R
import com.example.smartwaiter.clientBranch.ui.accountClient.AccountClient
import com.example.smartwaiter.inteface.*
import com.google.firebase.firestore.FirebaseFirestore


class ManagementClientFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    lateinit var client: Client

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_management_client, container, false)
        val imgProfileClient = view.findViewById<ImageView>(R.id.imgProfileClient)
        val txtNameUsr = view.findViewById<TextView>(R.id.txtNameUsr)
        val txtAccountUser = view.findViewById<TextView>(R.id.txtAccountUser)
        imgProfileClient.setImageResource(R.drawable.profiledefaultimage);


        db.collection("users").document(prefs.getCorreo()).get().addOnSuccessListener {
            txtNameUsr.text = it.get("usrName") as String
        }

        txtAccountUser.setOnClickListener {
            val intent = Intent(context, AccountClient::class.java)
            startActivity(intent)
        }


        return view
    }


}