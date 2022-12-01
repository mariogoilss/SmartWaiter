package com.example.smartwaiter.Prefs

import android.content.Context

class Prefs (val context: Context) {

    val SHARED_NAME = "Mydtb"
    val SHARED_EMAIL = "email"
    val SHARED_RECOR = "recor"
    val SHARED_IDORG = "idorg"
    val SHARED_TABLE = "table"



    //CREAMOS LA BASE DE DATOS
    val storage = context.getSharedPreferences(SHARED_NAME, 0)


    /*
     * FUN GUARDA VALORES
     */
    fun saveCorreo(email:String){
        storage.edit().putString(SHARED_EMAIL,email).apply()
    }

    fun saveRecordar(recor:Boolean){
        storage.edit().putBoolean(SHARED_RECOR, recor).apply()
    }

    fun saveOrgId(orgId:String){
        storage.edit().putString(SHARED_IDORG,orgId).apply()
    }

    fun saveTable(table:Int){
        storage.edit().putInt(SHARED_TABLE,table).apply()
    }



    /*
     * FUN DEVUELVE VALORES
     */

    fun getCorreo():String{
        return storage.getString(SHARED_EMAIL, "")!!
    }

    fun getRecor():Boolean{
        return  storage.getBoolean(SHARED_RECOR, false)
    }

    fun getOrgId():String{
        return  storage.getString(SHARED_IDORG,"")!!
    }

    fun getTable():Int{
        return storage.getInt(SHARED_TABLE, 0)
    }

    fun wipeOrders(){
        saveOrgId("")
        saveTable(0)
    }

    fun wipe(){
        storage.edit().clear().apply()
    }
}