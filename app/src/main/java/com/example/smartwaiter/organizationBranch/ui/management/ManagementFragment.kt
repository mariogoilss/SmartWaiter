package com.example.smartwaiter.organizationBranch.ui.management

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.smartwaiter.MainActivity
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.R
import com.example.smartwaiter.databinding.FragmentManagementBinding
import com.example.smartwaiter.databinding.FragmentMenuBinding
import com.example.smartwaiter.organizationBranch.MainOrganizationNav
import com.example.smartwaiter.organizationBranch.ui.accountOrganization.AccountOrganizationActivity
import com.example.smartwaiter.organizationBranch.ui.suggestionMailBox.SuggestionMailBoxActivity
import com.example.smartwaiter.organizationBranch.ui.tables.TablesActivity
import com.google.android.material.tabs.TabLayout
import org.w3c.dom.Text

class ManagementFragment : Fragment() {

    private var _binding: FragmentManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManagementBinding.inflate(inflater, container, false)
        val btnCloseSession:Button = binding.btnMngCloseSession
        val btnManageTables:TextView = binding.btnManageTables
        val btnManageAccount:TextView = binding.btnManageAccount
        val btnSuggestionMail:TextView = binding.btnSuggestionMail

        val image:ImageView = binding.imgPrueba
        image.setImageResource(R.drawable.grafica);
        val root: View = binding.root

        btnManageAccount.setOnClickListener {
            val intent = Intent(context, AccountOrganizationActivity::class.java)
            startActivity(intent)
        }

        btnManageTables.setOnClickListener {
            val intent = Intent(context, TablesActivity::class.java)
            startActivity(intent)
        }

        btnCloseSession.setOnClickListener {
            prefs.wipe()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        btnSuggestionMail.setOnClickListener {
            val intent = Intent(context, SuggestionMailBoxActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}