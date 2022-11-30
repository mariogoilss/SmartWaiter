package com.example.smartwaiter.adapters
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.R
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.Organization
import com.example.smartwaiter.inteface.SalesList


import com.google.firebase.firestore.FirebaseFirestore

private val db = FirebaseFirestore.getInstance()
class AdapterTableOrgRV : RecyclerView.Adapter<AdapterTableOrgRV.ViewHolder>(){


    var tableList: ArrayList<Int> = ArrayList()
    lateinit var tablesActivity: Activity
    lateinit var context: Context


    fun AdapterTableOrgRV(
        tableList: ArrayList<Int>,
        context: Context,
        tablesActivity: Activity
    ) {
        this.tableList = tableList
        this.context = context
        this.tablesActivity = tablesActivity
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tableList.get(position)
        holder.bind(item, context, this, position,tablesActivity )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.card_tables_item,parent, false)
        )
    }

    override fun getItemCount(): Int {
        return tableList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var idTable = view.findViewById(R.id.txtIdTable) as TextView
        var btnDelete = view.findViewById(R.id.btnDeleteTable) as ImageButton
        var btnPrint = view.findViewById(R.id.btnPrintTable) as ImageButton

        fun bind(
            id: Int,
            context: Context,
            adapter: AdapterTableOrgRV,
            pos: Int,
            tablesActivity: Activity
        ) {
            idTable.text = id.toString()

            btnDelete.setOnClickListener {
                Toast.makeText(context, "pos -> " + pos, Toast.LENGTH_SHORT).show()
                mostrar_emergente(context, pos, adapter)
            }

            btnPrint.setOnClickListener {
                printPdf(id, context, tablesActivity)
            }
        }

        private fun printPdf(idTable: Int, context: Context, tablesActivity:Activity){
            var titulo = "Mesa $idTable"

            if (!checkPermission(context)){
                requestPermissions(tablesActivity)
            }else{
                var pdfDocument = PdfDocument()
                var pdfTitle = TextPaint()
                var pdfQr = Paint()

                var pageInfo = PdfDocument.PageInfo.Builder(200, 200, 1).create()
                var page = pdfDocument.startPage(pageInfo)


            }
        }
        private fun checkPermission(context: Context):Boolean{
            val permissionWriteExt = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE)
            val permissionReadExt = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE)
            return permissionWriteExt == PackageManager.PERMISSION_GRANTED && permissionReadExt == PackageManager.PERMISSION_GRANTED
        }

        private fun requestPermissions(tablesActivity: Activity) {
            ActivityCompat.requestPermissions(
                tablesActivity,
                arrayOf(WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE),
                200
            )
        }


        private fun getOfBBDD(pos: Int, adapter:AdapterTableOrgRV) {
            db.collection("organizations").document(prefs.getCorreo()).get().addOnSuccessListener {

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

                organization.orgTablesList.removeAt(pos)


                adapter.tableList.removeAt(pos)
                adapter.notifyItemRemoved(pos)
                adapter.notifyItemRangeChanged( 0, adapter.tableList.size)

                saveOnBBDD(organization)
            }


        }

        private fun saveOnBBDD(organization: Organization) {
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

        fun mostrar_emergente(context:Context,pos: Int, adapter:AdapterTableOrgRV){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Alerta")
            builder.setMessage("¿Desea eliminar esta mesa?")
            builder.setPositiveButton("Si",{ dialogInterface: DialogInterface, i: Int -> getOfBBDD(pos,adapter) })
            builder.setNegativeButton("No",{ dialogInterface: DialogInterface, i: Int -> })
            builder.show()
        }

    }













}
