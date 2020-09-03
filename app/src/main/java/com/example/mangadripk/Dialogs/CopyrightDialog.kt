package com.example.mangadripk.Dialogs

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment


class CopyrightDialog : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle("Manga Not Available")
            .setMessage("Unfortuately, this source has been copyrighted, so you have to change sources to see if its available elsewhere")
            .setPositiveButton("That's tuff",
                DialogInterface.OnClickListener { dialogInterface, i -> })
        return builder.create()
    }
}