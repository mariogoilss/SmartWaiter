package com.example.smartwaiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.*
import com.example.smartwaiter.menu.arrayDrinkListOrg
import com.example.smartwaiter.menu.arrayFoodListOrg
import com.google.firebase.firestore.FirebaseFirestore


private val db = FirebaseFirestore.getInstance()
class AdapterMenuOrgRV : RecyclerView.Adapter<AdapterMenuOrgRV.ViewHolder>(){


    var itemMenuList: ArrayList<MenuItem> = ArrayList()
    lateinit var context: Context
    var foodOrDrink: Boolean = false

    fun AdapterMenuOrgRV(itemMenuList: ArrayList<MenuItem>, context: Context, foodOrDrink:Boolean) {
        this.itemMenuList = itemMenuList
        this.context = context
        this.foodOrDrink = foodOrDrink
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemMenuList.get(position)
        holder.bind(item, context, this, position, foodOrDrink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.card_menu_org,parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemMenuList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var name = view.findViewById(R.id.txtNameMenuCard) as TextView
        var image = view.findViewById(R.id.imgMenuCard) as ImageView
        var price = view.findViewById(R.id.txtPriceMenuCard) as TextView

        fun bind(itemMenu: MenuItem, context: Context, adapter: AdapterMenuOrgRV, pos: Int, foodOrDrink: Boolean) {

            name.text = itemMenu.name
            image = findImage(image, itemMenu, foodOrDrink)
            price.text = itemMenu.price.toString() + "€"

            itemView.setOnClickListener(View.OnClickListener {
                loadDrinkFragment(context, pos, itemMenu, adapter,foodOrDrink)
            })

        }

        private fun loadDrinkFragment(context: Context, pos: Int, itemMenu: MenuItem, adapter: AdapterMenuOrgRV, foodOrDrink:Boolean){
            val builder = AlertDialog.Builder(context)

            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.dialog_menu_item_add,null)

            // Fragments Elements
            val name = view.findViewById<TextView>(R.id.txtNameMenuDialog)!!
            val description = view.findViewById<TextView>(R.id.txtDescMenuDialog)!!
            val price = view.findViewById<TextView>(R.id.txtPriceMenuDialog)!!
            val save = view.findViewById<TextView>(R.id.btnSaveMenuDialog)!!
            val amount = view.findViewById<TextView>(R.id.txtAmountMenuDialog)!!

            // Se rellenan los campos del Fragment
            name.text = itemMenu.name
            description.text = itemMenu.description
            price.text = itemMenu.price.toString()
            amount.text = itemMenu.amountStock.toString()
            check_load(view,itemMenu.allergens as ArrayList<Int>)

            // Fragment Show
            builder.setView(view)
            val dialog = builder.create() //<- se crea el dialog
            dialog.show() //<- se muestra el showdialog

            // Fragment Functions
            save.setOnClickListener {
                var menuItem = MenuItem(name.text.toString(), description.text.toString(), price.text.toString().toDouble(),check_checkBox(view),"", amount.text.toString().toInt() )
                getOfBBDD(PreLoad.prefs.getCorreo(), menuItem,pos, adapter, foodOrDrink)
                dialog.onBackPressed()
            }
        }

        fun check_load(view: View,allergensList: ArrayList<Int>){
            val cbx1 = view.findViewById<CheckBox>(R.id.checkBox1)!!
            val cbx2 = view.findViewById<CheckBox>(R.id.checkBox2)!!
            val cbx3 = view.findViewById<CheckBox>(R.id.checkBox3)!!
            val cbx4 = view.findViewById<CheckBox>(R.id.checkBox4)!!
            val cbx5 = view.findViewById<CheckBox>(R.id.checkBox5)!!
            val cbx6 = view.findViewById<CheckBox>(R.id.checkBox6)!!
            val cbx7 = view.findViewById<CheckBox>(R.id.checkBox7)!!
            val cbx8 = view.findViewById<CheckBox>(R.id.checkBox8)!!

            for(i in 0 until allergensList.size){

                if(allergensList[i].toInt() == 1){cbx1.isChecked = true}
                if(allergensList[i].toInt() == 2){cbx2.isChecked = true}
                if(allergensList[i].toInt() == 3){cbx3.isChecked = true}
                if(allergensList[i].toInt() == 4){cbx4.isChecked = true}
                if(allergensList[i].toInt() == 5){cbx5.isChecked = true}
                if(allergensList[i].toInt() == 6){cbx6.isChecked = true}
                if(allergensList[i].toInt() == 7){cbx7.isChecked = true}
                if(allergensList[i].toInt() == 8){cbx8.isChecked = true}

            }

        }

        fun check_checkBox(view: View): ArrayList<Int>{
            val cbx1 = view.findViewById<CheckBox>(R.id.checkBox1)!!
            val cbx2 = view.findViewById<CheckBox>(R.id.checkBox2)!!
            val cbx3 = view.findViewById<CheckBox>(R.id.checkBox3)!!
            val cbx4 = view.findViewById<CheckBox>(R.id.checkBox4)!!
            val cbx5 = view.findViewById<CheckBox>(R.id.checkBox5)!!
            val cbx6 = view.findViewById<CheckBox>(R.id.checkBox6)!!
            val cbx7 = view.findViewById<CheckBox>(R.id.checkBox7)!!
            val cbx8 = view.findViewById<CheckBox>(R.id.checkBox8)!!
            var allergensList: ArrayList<Int> = arrayListOf()

            if (cbx1.isChecked) {allergensList.add(1)}
            if (cbx2.isChecked) {allergensList.add(2)}
            if (cbx3.isChecked) {allergensList.add(3)}
            if (cbx4.isChecked) {allergensList.add(4)}
            if (cbx5.isChecked) {allergensList.add(5)}
            if (cbx6.isChecked) {allergensList.add(6)}
            if (cbx7.isChecked) {allergensList.add(7)}
            if (cbx8.isChecked) {allergensList.add(8)}

            return allergensList

        }

        private fun getOfBBDD(
            id: String,
            menuItem: MenuItem,
            pos: Int,
            adapter: AdapterMenuOrgRV,
            foodOrDrink: Boolean
        ){
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
                        bankAccount)

                if(foodOrDrink){
                    organization.orgFoodList[pos] = menuItem
                    arrayFoodListOrg[pos] = menuItem
                }else{
                    organization.orgDrinkList[pos] = menuItem
                    arrayDrinkListOrg[pos] = menuItem
                }


                adapter.notifyItemChanged(pos)
                saveOnBBDD(organization)
            }
        }

        private fun saveOnBBDD(organization: Organization){
            db.collection("organizations").document(PreLoad.prefs.getCorreo()).set(
                hashMapOf(
                    "orgName" to organization.orgName,
                    "orgCif" to organization.orgCif,
                    "orgFoodList" to organization.orgFoodList,
                    "orgDrinkList" to organization.orgDrinkList,
                    "orgOpenOrNot" to organization.orgOpenOrNot,
                    "orgSalesList" to organization.orgSalesList,
                    "orgBankAccount" to organization.orgBankAccount
                )
            )
        }

        private fun findImage(image: ImageView, itemMenu: MenuItem, foodOrDrink: Boolean): ImageView {
            if (itemMenu.image == ""){
                if (foodOrDrink){
                    image.setImageResource(R.drawable.comida);
                }else{
                    image.setImageResource(R.drawable.refresco);
                }
            }else{
                // desarrollar recuperar imagen
            }
            return image
        }


    }





}
