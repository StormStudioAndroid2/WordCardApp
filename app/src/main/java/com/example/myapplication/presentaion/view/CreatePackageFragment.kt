package com.example.myapplication.presentaion.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R

interface CreatePackageCallback {
    fun onPackageFragmentYes(title: String, firstLanguage: String, secondLanguage: String)
}

class CreatePackageFragment : DialogFragment() {



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.create_package_layout, null)

            builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.yes, null)
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create().apply {
                setOnShowListener {
                    val button: Button =
                        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                    button.setOnClickListener {
                        val name = view.findViewById<EditText>(R.id.card_set_name).text
                        val firstLanguage =
                            view.findViewById<EditText>(R.id.first_language_package).text
                        val secondLanguage =
                            view.findViewById<EditText>(R.id.second_language_package).text
                        if (name.isNotEmpty() && firstLanguage.isNotEmpty() && secondLanguage.isNotEmpty()) {
                            (activity as? CreatePackageCallback)?.onPackageFragmentYes(
                                name.toString(),
                                firstLanguage.toString(),
                                secondLanguage.toString()
                            )
                            dismiss()
                        } else {
                            view.findViewById<TextView>(R.id.package_error).visibility =
                                View.VISIBLE
                        }

                    }
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}