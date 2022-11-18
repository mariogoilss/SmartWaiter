package com.example.smartwaiter.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.Organization
import com.google.firebase.firestore.FirebaseFirestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DrinkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrinkFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_drink, container, false)
        var addDrink = view.findViewById<ImageButton>(R.id.btnAddDrink)
        addDrink.setOnClickListener {
            Toast.makeText(context, "si", Toast.LENGTH_SHORT).show()
            loadDrinkFragment()
        }
        return view
    }

    private fun loadDrinkFragment(){
        val builder = AlertDialog.Builder(context!!)
        val view = layoutInflater.inflate(R.layout.dialog_menu_item_add,null)


        // Fragments Elements
        val name = view.findViewById<TextView>(R.id.txtNameMenuDialog)!!
        val description = view.findViewById<TextView>(R.id.txtDescMenuDialog)!!
        val cbx1 = view.findViewById<CheckBox>(R.id.checkBox1)!!
        val cbx2 = view.findViewById<CheckBox>(R.id.checkBox2)!!
        val cbx3 = view.findViewById<CheckBox>(R.id.checkBox3)!!
        val cbx4 = view.findViewById<CheckBox>(R.id.checkBox4)!!
        val cbx5 = view.findViewById<CheckBox>(R.id.checkBox5)!!
        val cbx6 = view.findViewById<CheckBox>(R.id.checkBox6)!!
        val cbx7 = view.findViewById<CheckBox>(R.id.checkBox7)!!
        val cbx8 = view.findViewById<CheckBox>(R.id.checkBox8)!!
        val price = view.findViewById<TextView>(R.id.txtPriceMenuDialog)!!
        val save = view.findViewById<TextView>(R.id.btnSaveMenuDialog)!!

        // Fragment Show
        builder.setView(view)
        val dialog = builder.create() //<- se crea el dialog
        dialog.show() //<- se muestra el showdialog

        // Fragment Functions
        // check_checkBox(cbx1, cbx2,cbx3,cbx4,cbx5, cbx6, cbx7,cbx8)

    }


    private fun getOfBBDD(ide:String): Organization{
        lateinit var organization: Organization
        db.collection("organizations").document(ide).get().addOnSuccessListener {
            organization.orgName = it.get("orgName") as String
            organization.orgCif = it.get("orgCif") as String
            organization.orgFoodList = it.get("orgFoodList") as ArrayList<MenuItem>
            organization.orgDrinkList = it.get("orgDrinkList") as ArrayList<MenuItem>
            organization.orgFirstInit = it.get("orgFirstInit") as Boolean
        }
        return organization
    }



    fun check_checkBox(cbx1:CheckBox, cbx2:CheckBox,cbx3:CheckBox,cbx4:CheckBox,cbx5:CheckBox,
    cbx6:CheckBox, cbx7:CheckBox,cbx8:CheckBox): ArrayList<Int>{
        var alergensList: ArrayList<Int> = arrayListOf()

        if (cbx1.isChecked) {alergensList.add(1)}
        if (cbx2.isChecked) {alergensList.add(2)}
        if (cbx3.isChecked) {alergensList.add(3)}
        if (cbx4.isChecked) {alergensList.add(4)}
        if (cbx5.isChecked) {alergensList.add(5)}
        if (cbx6.isChecked) {alergensList.add(6)}
        if (cbx7.isChecked) {alergensList.add(7)}
        if (cbx8.isChecked) {alergensList.add(8)}

        return alergensList

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DrinkFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DrinkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}