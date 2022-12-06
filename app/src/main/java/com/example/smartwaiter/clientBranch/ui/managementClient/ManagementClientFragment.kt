package com.example.smartwaiter.clientBranch.ui.managementClient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.smartwaiter.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ManagementClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManagementClientFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_management_client, container, false)
        val imgProfileClient = view.findViewById<ImageView>(R.id.imgProfileClient)
        imgProfileClient.setImageResource(R.drawable.profiledefaultimage);
        return view
    }


}