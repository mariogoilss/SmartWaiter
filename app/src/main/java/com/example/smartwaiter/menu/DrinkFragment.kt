package com.example.smartwaiter.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.R
import com.example.smartwaiter.adapters.AdapterMenuOrgRV
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.Organization
import com.example.smartwaiter.inteface.SalesList
import com.google.firebase.firestore.FirebaseFirestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/*
 * DECLARAMOS LA VARIBLES NECESARIAS PARA UTILIZAR
 * RECYCLERVIEW @MiApadatorRVeven
 */
private lateinit var recyclerViewDrinkListOrg: RecyclerView
private val adapterMenuOrgRV: AdapterMenuOrgRV = AdapterMenuOrgRV()
var arrayDrinkListOrg = ArrayList<MenuItem>()//<-- Declaramos el arrayList a devolver

/**
 * A simple [Fragment] subclass.
 * Use the [DrinkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrinkFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private var param1: String? = null
    private var param2: String? = null
    private var idOrganization  = "";

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
        if (prefs.getOrgOrUser()){
            idOrganization = prefs.getCorreo()
        }else{
            idOrganization = prefs.getOrgId()
        }

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_drink, container, false)
        var addDrink = view.findViewById<ImageButton>(R.id.btnAddDrink)

        addDrink.setOnClickListener {
            loadDrinkFragment()
        }

        if (arrayDrinkListOrg.isEmpty()) {
            load(view)
        } else {
            recyclerViewDrinkListOrg = view.findViewById<RecyclerView>(R.id.rvMenuDrinkOrg)
            recyclerViewDrinkListOrg.setHasFixedSize(true)
            recyclerViewDrinkListOrg.layoutManager = LinearLayoutManager(context!!)
            adapterMenuOrgRV.AdapterMenuOrgRV(arrayDrinkListOrg, context!!, false)
            recyclerViewDrinkListOrg.adapter = adapterMenuOrgRV
        }
        return view
    }

    private fun load(view: View) {
        db.collection("organizations").document(idOrganization).get().addOnSuccessListener {

            var arrayToHash = ArrayList<MenuItem>()
            arrayToHash =
                it.get("orgDrinkList") as ArrayList<MenuItem> //<-- Pillamos tabla hash BBDD


            for (i in 0 until arrayToHash.size) {
                val x = arrayToHash[i] as HashMap<String, String>

                var menuItem = MenuItem(
                    x.getValue("name"),
                    x.getValue("description"),
                    x.getValue("price") as Double,
                    x.getValue("allergens") as ArrayList<Int>,
                    x.getValue("image"),
                    (x.getValue("amountStock") as Long).toInt()
                )

                arrayDrinkListOrg.add(menuItem)
            }

            recyclerViewDrinkListOrg = view.findViewById<RecyclerView>(R.id.rvMenuDrinkOrg)
            recyclerViewDrinkListOrg.setHasFixedSize(true)
            recyclerViewDrinkListOrg.layoutManager = LinearLayoutManager(context!!)
            adapterMenuOrgRV.AdapterMenuOrgRV(arrayDrinkListOrg, context!!, false)
            recyclerViewDrinkListOrg.adapter = adapterMenuOrgRV


        }

    }

    private fun loadDrinkFragment() {
        val builder = AlertDialog.Builder(context!!)
        val view = layoutInflater.inflate(R.layout.dialog_menu_item_add, null)

        // Fragments Elements
        val name = view.findViewById<TextView>(R.id.txtNameMenuDialog)!!
        val description = view.findViewById<TextView>(R.id.txtDescMenuDialog)!!
        val amount = view.findViewById<TextView>(R.id.txtAmountMenuDialog)!!
        val price = view.findViewById<TextView>(R.id.txtPriceMenuDialog)!!
        val save = view.findViewById<TextView>(R.id.btnSaveMenuDialog)!!

        // Fragment Show
        builder.setView(view)
        val dialog = builder.create() //<- se crea el dialog
        dialog.show() //<- se muestra el showdialog

        // Fragment Functions
        // check_checkBox(cbx1, cbx2,cbx3,cbx4,cbx5, cbx6, cbx7,cbx8)
        save.setOnClickListener {
            var menuItem = MenuItem(
                name.text.toString(),
                description.text.toString(),
                price.text.toString().toDouble(),
                check_checkBox(view),
                "",
                amount.text.toString().toInt()
            )
            getOfBBDD(idOrganization, menuItem)
            dialog.onBackPressed()
        }


    }


    private fun getOfBBDD(id: String, menuItem: MenuItem) {


        db.collection("organizations").document(id).get().addOnSuccessListener {

            var arrayToHash = it.get("orgBankAccount") as HashMap<String, String> //<-- Pillamos tabla hash BBDD
            var bankAccount = BankAccount(
                arrayToHash.getValue("account"),
                arrayToHash.getValue("expirationDate"),
                arrayToHash.getValue("secretNumber")
            )

            var organization =
                Organization(it.get("orgName") as String,
                    it.get("orgCif") as String,
                    it.get("orgFoodList") as ArrayList<MenuItem>,
                    it.get("orgDrinkList") as ArrayList<MenuItem>,
                    it.get("orgOpenOrNot") as Boolean,
                    it.get("orgSalesList") as ArrayList<SalesList>,
                    bankAccount,
                    it.get("orgSuggestionsMailBox") as ArrayList<String>,
                    it.get("orgTablesList") as ArrayList<Int>)

            organization.orgDrinkList.add(menuItem) //<- guardamos el nuevo item

            arrayDrinkListOrg.add(menuItem)
            adapterMenuOrgRV.notifyItemInserted(arrayDrinkListOrg.size)
            adapterMenuOrgRV.notifyItemRangeChanged(arrayDrinkListOrg.size, arrayDrinkListOrg.size)


            saveOnBBDD(organization)

        }

    }

    private fun saveOnBBDD(organization: Organization) {
        db.collection("organizations").document(idOrganization).set(
            hashMapOf(
                "orgName" to organization.orgName,
                "orgCif" to organization.orgCif,
                "orgFoodList" to organization.orgFoodList,
                "orgDrinkList" to organization.orgDrinkList,
                "orgOpenOrNot" to organization.orgOpenOrNot,
                "orgSalesList" to organization.orgSalesList,
                "orgBankAccount" to organization.orgBankAccount,
                "orgSuggestionsMailBox" to organization.orgSuggestionsMailBox,
                "orgTablesList" to organization.orgTablesList
            )
        )
    }

    fun check_checkBox(view: View): ArrayList<Int> {
        val cbx1 = view.findViewById<CheckBox>(R.id.checkBox1)!!
        val cbx2 = view.findViewById<CheckBox>(R.id.checkBox2)!!
        val cbx3 = view.findViewById<CheckBox>(R.id.checkBox3)!!
        val cbx4 = view.findViewById<CheckBox>(R.id.checkBox4)!!
        val cbx5 = view.findViewById<CheckBox>(R.id.checkBox5)!!
        val cbx6 = view.findViewById<CheckBox>(R.id.checkBox6)!!
        val cbx7 = view.findViewById<CheckBox>(R.id.checkBox7)!!
        val cbx8 = view.findViewById<CheckBox>(R.id.checkBox8)!!
        var allergensList: ArrayList<Int> = arrayListOf()

        if (cbx1.isChecked) {
            allergensList.add(1)
        }
        if (cbx2.isChecked) {
            allergensList.add(2)
        }
        if (cbx3.isChecked) {
            allergensList.add(3)
        }
        if (cbx4.isChecked) {
            allergensList.add(4)
        }
        if (cbx5.isChecked) {
            allergensList.add(5)
        }
        if (cbx6.isChecked) {
            allergensList.add(6)
        }
        if (cbx7.isChecked) {
            allergensList.add(7)
        }
        if (cbx8.isChecked) {
            allergensList.add(8)
        }

        return allergensList

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