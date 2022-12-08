package com.example.smartwaiter.clientBranch.basketUtils
import android.content.Context
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.adapters.AdapterBasketRV
import com.example.smartwaiter.inteface.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BasketUtils {





    companion object{
        private val db = FirebaseFirestore.getInstance()
        var saleItemList = ArrayList<SaleItem>()



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

        fun getOfBBDD(adapterBasketRV: AdapterBasketRV) {
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

                var salesList = SalesList(Date().toString(),saleItemList,false, prefs.getTable().toLong(),benefit())
                organization.orgSalesList.add(salesList) //<- guardamos el nuevo item

                saveOnBBDD(organization, adapterBasketRV)
            }

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