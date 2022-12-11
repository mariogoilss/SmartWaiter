package com.example.smartwaiter.organizationBranch.ui.management

import android.R
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartwaiter.MainActivity
import com.example.smartwaiter.Prefs.PreLoad
import com.example.smartwaiter.Prefs.PreLoad.Companion.prefs
import com.example.smartwaiter.databinding.FragmentManagementBinding
import com.example.smartwaiter.inteface.BankAccount
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.Organization
import com.example.smartwaiter.inteface.SalesList
import com.example.smartwaiter.organizationBranch.dateUtils.DateUtils
import com.example.smartwaiter.organizationBranch.ui.accountOrganization.AccountOrganizationActivity
import com.example.smartwaiter.organizationBranch.ui.suggestionMailBox.SuggestionMailBoxActivity
import com.example.smartwaiter.organizationBranch.ui.tables.TablesActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ManagementFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManagementBinding.inflate(inflater, container, false)
        val btnCloseSession:Button = binding.btnMngCloseSession
        val btnMonthLeft:ImageButton = binding.btnMonthLeft
        val btnMonthRight:ImageButton = binding.btnMonthRight
        val btnManageTables:TextView = binding.btnManageTables
        val btnManageAccount:TextView = binding.btnManageAccount
        val btnSuggestionMail:TextView = binding.btnSuggestionMail
        val txtMonthManagement:EditText = binding.txtMonthManagement
        val barChar:BarChart = binding.barChart
        val root: View = binding.root


        loadGrap(barChar, DateUtils.actualDate)
        txtMonthManagement.setText("" +LocalDate.now().month.toString() + ", " +LocalDate.now().year)
        txtMonthManagement.isFocusable = false

        btnMonthLeft.setOnClickListener {

            var valueMonth = DateUtils.actualDate.month.value - 1
            if (valueMonth < 1){
                DateUtils.actualDate = LocalDate.parse("" + (DateUtils.actualDate.year-1)+"-12-10")
                loadGrap(barChar, DateUtils.actualDate)
                txtMonthManagement.setText("DECEMBER" + ", " +DateUtils.actualDate.year)

            }else{
                if ((DateUtils.actualDate.month.value - 1).toString().length == 1){
                    DateUtils.actualDate = LocalDate.parse("" + DateUtils.actualDate.year + "-0"+( DateUtils.actualDate.month.value - 1)+ "-12")
                    loadGrap(barChar, DateUtils.actualDate)
                    txtMonthManagement.setText("" + DateUtils.actualDate.month.toString() + ", " +DateUtils.actualDate.year)
                }else{
                    DateUtils.actualDate = LocalDate.parse("" + DateUtils.actualDate.year + "-"+( DateUtils.actualDate.month.value - 1)+ "-12")
                    loadGrap(barChar, DateUtils.actualDate)
                    txtMonthManagement.setText("" + DateUtils.actualDate.month.toString() + ", " +DateUtils.actualDate.year)
                }

            }

        }

        btnMonthRight.setOnClickListener {

            var valueMonth = DateUtils.actualDate.month.value + 1

            if (valueMonth > 12){
                DateUtils.actualDate = LocalDate.parse("" + (DateUtils.actualDate.year+1)+"-01-10")
                loadGrap(barChar, DateUtils.actualDate)
                txtMonthManagement.setText("JANUARY" + ", " +DateUtils.actualDate.year)

            }else{
                if ((DateUtils.actualDate.month.value + 1).toString().length == 1){
                    DateUtils.actualDate = LocalDate.parse("" + DateUtils.actualDate.year + "-0"+( DateUtils.actualDate.month.value + 1)+ "-12")
                    loadGrap(barChar, DateUtils.actualDate)
                    txtMonthManagement.setText("" + DateUtils.actualDate.month.toString() + ", " +DateUtils.actualDate.year)
                }else{
                    DateUtils.actualDate = LocalDate.parse("" + DateUtils.actualDate.year + "-"+( DateUtils.actualDate.month.value + 1)+ "-12")
                    loadGrap(barChar, DateUtils.actualDate)
                    txtMonthManagement.setText("" + DateUtils.actualDate.month.toString() + ", " +DateUtils.actualDate.year)
                }

            }

        }

        btnManageAccount.setOnClickListener {
            val intent = Intent(context, AccountOrganizationActivity::class.java)
            startActivity(intent)
        }

        btnManageTables.setOnClickListener {
            val intent = Intent(context, TablesActivity::class.java)
            startActivity(intent)
        }

        btnCloseSession.setOnClickListener {
            prefs.wipe()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        btnSuggestionMail.setOnClickListener {
            val intent = Intent(context, SuggestionMailBoxActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun loadGrap(barChar: BarChart, now: LocalDate) {

        db.collection("organizations").document(PreLoad.prefs.getCorreo()).get().addOnSuccessListener {


            var arrayToHash = it.get("orgBankAccount") as HashMap<String, String> //<-- Pillamos tabla hash BBDD
            var bankAccount = BankAccount(arrayToHash.getValue("account"), arrayToHash.getValue("expirationDate"), arrayToHash.getValue("secretNumber")
            )

            var organization = Organization(it.get("orgName") as String,
                    it.get("orgCif") as String,
                    it.get("orgFoodList") as ArrayList<MenuItem>,
                    it.get("orgDrinkList") as ArrayList<MenuItem>,
                    it.get("orgOpenOrNot") as Boolean,
                    it.get("orgSalesList") as ArrayList<SalesList>,
                    bankAccount,
                    it.get("orgSuggestionsMailBox") as ArrayList<String>,
                    it.get("orgTablesList") as ArrayList<Int>)


            var lastDay = now.with(TemporalAdjusters.lastDayOfMonth());
            var listFloat:ArrayList<Float>  = ArrayList()

            for (i in 0 until lastDay.dayOfMonth){
                listFloat.add(0f)
            }

            for (i in 0 until organization.orgSalesList.size){
                var hash = organization.orgSalesList[i] as HashMap<String, String> //<-- Pillamos tabla hash BBDD

                val list: List<String> = listOf(*hash.get("date")!!.split(" ").toTypedArray())
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                val dateBBDD = LocalDate.parse(list[0], formatter);


                for(x in 0 until  listFloat.size){
                    if (dateBBDD.month.value == now.month.value && dateBBDD.year == now.year && dateBBDD.dayOfMonth == x ){
                        listFloat[x] = listFloat[now.dayOfMonth ] + (hash.get("benefit") as Double).toFloat()
                    }
                }

            }

            var list:ArrayList<BarEntry>  = ArrayList()
            for (h in 1 until listFloat.size){
                list.add(BarEntry(h.toFloat(),listFloat[h]))
            }

            var barDataSet  = BarDataSet(list,"Beneficios")
            barDataSet.valueTextColor = Color.BLACK
            barDataSet.valueTextSize = 6f
            barDataSet.color
            barDataSet.setColor(resources.getColor(R.color.holo_orange_dark))

            val barData = BarData(barDataSet)
            barChar.data = barData
            barChar.description.isEnabled = false


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}