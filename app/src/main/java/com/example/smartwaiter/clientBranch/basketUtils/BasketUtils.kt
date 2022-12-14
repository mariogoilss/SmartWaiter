package com.example.smartwaiter.clientBranch.basketUtils
import android.content.Context
import android.widget.Toast
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.adapters.AdapterBasketRV
import com.example.smartwaiter.inteface.*
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BasketUtils {





    companion object{
        private val db = FirebaseFirestore.getInstance()
        var saleItemList = ArrayList<SaleItem>()
        var idOrganizations = ""


        fun saveOnShopList(context: Context){

            db.collection("users").document(prefs.getCorreo()).get().addOnSuccessListener {

                var arrayToHash = it.get("usrBankAccount") as HashMap<String, String> //<-- Pillamos tabla hash BBDD
                var bankAccount = BankAccount(arrayToHash.getValue("account"), arrayToHash.getValue("expirationDate"), arrayToHash.getValue("secretNumber"))
                var client = Client(it.get("usrName") as String, it.get("usrImageProfile") as String, bankAccount, it.get("usrShopList") as ArrayList<ShopInfo>)


                var comparator = true
                var position = 0
                for (index in 0 until client.shopList.size){
                    var hash = client.shopList[index] as HashMap<String, String>

                    if (idOrganizations == hash.getValue("idOrganization")){
                        comparator = false
                        position
                    }
                }

                if (!comparator){
                    client.shopList.removeAt(position)
                }


                db.collection("organizations").document(prefs.getOrgId()).get().addOnSuccessListener{
                    var nameOrganization = it.get("orgName") as String
                    var sale =  ShopInfo(idOrganizations, nameOrganization)

                    client.shopList.add(sale)

                    db.collection("users").document(prefs.getCorreo()).set(
                        hashMapOf(
                            "usrName" to client.name,
                            "usrImageProfile" to client.imgProfile,
                            "usrBankAccount" to client.bankAccount,
                            "usrShopList" to client.shopList

                        )
                    )
                }
            }
        }

        fun saveSaleItemList(menuItem: MenuItem, context:Context){
            var amount:Int = 0
            var checker = true

            if(saleItemList.size > 0){
                for(i in 0 until saleItemList.size){
                    if (saleItemList[i].menuItem == menuItem){
                        saleItemList[i].amount = saleItemList[i].amount + 1
                        saleItemList[i].totalPrice = saleItemList[i].amount *  saleItemList[i].menuItem.price
                        checker = false
                    }
                }
                if (checker){
                    amount = 1
                    var saleItem = SaleItem(menuItem,amount, (menuItem.price*amount))
                    saleItemList.add(saleItem)
                }

            }else{
                amount = 1
                var saleItem = SaleItem(menuItem,amount, (menuItem.price*amount))
                saleItemList.add(saleItem)
            }
        }

        fun getOfBBDD(adapterBasketRV: AdapterBasketRV , context: Context) {
            db.collection("organizations").document(prefs.getOrgId()).get().addOnSuccessListener {

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



                val dateFormated = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
                var salesList = SalesList(dateFormated,saleItemList,false, prefs.getTable().toLong(),benefit())

                if (checker_stock(saleItemList,context, organization)){
                    organization.orgSalesList.add(salesList) //<- guardamos el nuevo item
                    organization.orgFoodList = minus_stock_food(organization.orgFoodList, saleItemList,context)
                    organization.orgDrinkList = minus_stock_drink(organization.orgDrinkList, saleItemList,context)
                    saveOnBBDD(organization, adapterBasketRV)
                    Toast.makeText(context, "Compra realizada", Toast.LENGTH_SHORT).show()
                }

            }

        }

        private fun minus_stock_food(orgFoodList: ArrayList<MenuItem>, saleItemList: ArrayList<SaleItem>, context: Context): ArrayList<MenuItem> {
            for (i in 0 until orgFoodList.size){
                var hash = orgFoodList[i] as HashMap<String, String> //<-- Pillamos tabla hash BBDD

                var menuObj = MenuItem(
                    hash["name"] as String,
                    hash["description"] as String,
                    hash["price"] as Double,
                    hash["allergens"] as ArrayList<Int>,
                    hash["image"] as String,
                    (hash["amountStock"] as Long).toInt())

                for(x in 0 until saleItemList.size){
                    if(menuObj.name == saleItemList[x].menuItem.name){
                        menuObj.amountStock -= saleItemList[x].amount
                    }
                }
                orgFoodList[i] = menuObj
            }
            return orgFoodList
        }

        private fun minus_stock_drink(orgDrinkList: ArrayList<MenuItem>, saleItemList: ArrayList<SaleItem>, context: Context): ArrayList<MenuItem> {
            for (i in 0 until orgDrinkList.size){
                var hash = orgDrinkList[i] as HashMap<String, String> //<-- Pillamos tabla hash BBDD

                var menuObj = MenuItem(
                    hash["name"] as String,
                    hash["description"] as String,
                    hash["price"] as Double,
                    hash["allergens"] as ArrayList<Int>,
                    hash["image"] as String,
                    (hash["amountStock"] as Long).toInt())

                for(x in 0 until saleItemList.size){
                    if(menuObj.name == saleItemList[x].menuItem.name){
                        menuObj.amountStock -= saleItemList[x].amount
                    }
                }
                orgDrinkList[i] = menuObj
            }
            return orgDrinkList
        }



        private fun checker_stock(saleItemList: ArrayList<SaleItem>, context: Context, organization: Organization):Boolean{
            var checker = true


            for (y in 0 until organization.orgFoodList.size){
                for (z in 0 until saleItemList.size){
                    var hash = organization.orgFoodList[y] as HashMap<String, String> //<-- Pillamos tabla hash BBDD

                    var menuObj = MenuItem(
                        hash["name"] as String,
                        hash["description"] as String,
                        hash["price"] as Double,
                        hash["allergens"] as ArrayList<Int>,
                        hash["image"] as String,
                        (hash["amountStock"] as Long).toInt())

                    if(menuObj.name == saleItemList[z].menuItem.name ){
                        if (menuObj.amountStock < saleItemList[z].amount){
                            Toast.makeText(context, "No hay suficiente stock del producto: ${menuObj.name}", Toast.LENGTH_SHORT).show()
                            checker = false
                        }
                    }
                }
            }


            for (i in 0 until organization.orgDrinkList.size){
                for (x in 0 until saleItemList.size){
                    var hash = organization.orgDrinkList[i] as HashMap<String, String> //<-- Pillamos tabla hash BBDD

                    var menuObj = MenuItem(
                        hash["name"] as String,
                        hash["description"] as String,
                        hash["price"] as Double,
                        hash["allergens"] as ArrayList<Int>,
                        hash["image"] as String,
                        (hash["amountStock"] as Long).toInt())

                    if(menuObj.name == saleItemList[x].menuItem.name ){
                        if (menuObj.amountStock < saleItemList[x].amount){
                            Toast.makeText(context, "No hay suficiente stock del producto: ${menuObj.name}", Toast.LENGTH_SHORT).show()
                            checker = false
                        }
                    }
                }
            }

            return checker
        }

        private fun benefit():Double{
            var benefit:Double = 0.0

            for(i in 0 until saleItemList.size){
                benefit += saleItemList[i].totalPrice
            }

            return benefit
        }

        private fun saveOnBBDD(organization: Organization, adapterBasketRV: AdapterBasketRV) {
            db.collection("organizations").document(prefs.getOrgId()).set(
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
            prefs.wipeOrders()
            adapterBasketRV.notifyItemRangeRemoved(0,BasketUtils.saleItemList.size)
            BasketUtils.saleItemList.clear()


        }
    }
}