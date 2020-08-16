package com.example.mangadripk.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.R


class Download : Fragment() {
    private val progressDialog = CustomProgressDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_download, container, false)
        activity?.let { progressDialog.show(it) }
        progressDialog.dialog.dismiss()
        return view
    }


}