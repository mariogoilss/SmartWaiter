package com.example.smartwaiter.clientBranch.ui.menuClient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.R
import com.example.smartwaiter.clientBranch.ui.cameraQr.CameraQrActivity
import com.example.smartwaiter.databinding.FragmentMenuBinding
import com.example.smartwaiter.menu.DrinkFragment
import com.example.smartwaiter.menu.FoodFragment
import com.example.smartwaiter.organizationBranch.MainOrganizationNav
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator


class MenuClientFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        var tabLayout: TabLayout = binding.tabLayout
        var frameLayout: FrameLayout = binding.frameLayout
        val root: View = binding.root


        //initScanner(this)
        if (prefs.getOrgId() == "" && prefs.getTable() == 0){
            loadCamera(context!! )

        }else{
            Toast.makeText(context, "entro", Toast.LENGTH_SHORT).show()
            initMenu(tabLayout)
        }


        return root
    }

    private fun loadCamera(context: Context){
        val builder = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.dialog_scan_qr,null)
        val btnCameraQR = view.findViewById<ImageButton>(R.id.btnCameraQR)!!
        builder.setView(view)

        btnCameraQR.setOnClickListener {
            val intent = Intent(context, CameraQrActivity::class.java)
            startActivity(intent)
        }

        val dialog = builder.create() //<- se crea el dialog
        dialog.show() //<- se muestra el showdialog



    }



    private fun initMenu(tabLayout: TabLayout){
        var fragment:Fragment
        fragment = FoodFragment()
        var fragmentManager = activity?.supportFragmentManager
        var fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction!!.replace(R.id.frameLayout, fragment)
        fragmentTransaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction!!.commit()


        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // creating cases for fragment
                when (tab.position) {
                    0 -> fragment = FoodFragment()
                    1 -> fragment = DrinkFragment()

                }
                val fm = activity?.supportFragmentManager
                val ft = fm?.beginTransaction()
                ft?.replace(R.id.frameLayout, fragment)
                ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft?.commit()

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}