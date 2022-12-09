package com.example.smartwaiter.clientBranch.ui.cameraQr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.R
import com.example.smartwaiter.clientBranch.MainClientActivityNav
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.Organization
import com.example.smartwaiter.inteface.SalesList
import com.example.smartwaiter.utils.UtilsBBDD
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
private val db = FirebaseFirestore.getInstance()
class CameraQrActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_qr)


        IntentIntegrator(this).initiateScan()
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setBeepEnabled(false)
        integrator.initiateScan()





    }

    fun stringToWords(s : String) = s.trim().splitToSequence(',')
        .filter { it.isNotEmpty() }.toList()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null){
            if (result.contents != null){
                var cad:String = result.contents.toString()
                if(cad.contains(",")){
                    try{
                        var array:ArrayList<String> = stringToWords(cad) as ArrayList<String>
                        prefs.saveOrgId(array[0])
                        prefs.saveTable(array[1].toInt())
                        
                        db.collection("organizations").document(array[0]).get().addOnSuccessListener {

                            var checker = it.get("orgOpenOrNot") as Boolean //<-- Pillamos tabla hash BBDD
                            
                            if(checker){
                                val intent = Intent(this, MainClientActivityNav::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this, "Este negocio no esta abierto.", Toast.LENGTH_SHORT).show()
                                onBackPressed()
                            }
                        }
                        
                        

                    }catch (e:Exception){
                        Toast.makeText(this, "Codigo QR no valido", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }else{
                    Toast.makeText(this, "Codigo QR no valido", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }else{
                onBackPressed()
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}