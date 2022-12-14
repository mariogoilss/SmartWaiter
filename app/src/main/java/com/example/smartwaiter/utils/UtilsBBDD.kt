package com.example.smartwaiter.utils

import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.inteface.Client
import com.example.smartwaiter.inteface.Organization
import com.google.firebase.firestore.FirebaseFirestore

class UtilsBBDD {

    companion object{
        private val db = FirebaseFirestore.getInstance()

        fun saveOnBBDD(organization: Organization){
            db.collection("organizations").document(PreLoad.prefs.getCorreo()).set(
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

        fun saveOnBBDDClient(client: Client){
            db.collection("users").document(PreLoad.prefs.getCorreo()).set(
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