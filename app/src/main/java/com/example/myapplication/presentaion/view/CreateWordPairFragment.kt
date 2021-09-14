package com.example.myapplication.presentaion.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R

interface CreateWordPairCallback {
    fun onWordPairFragmentYes(frontWord: String, backWord: String)
}

class CreateWordPairFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.create_word_pair_layout, null)

            builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.yes, null)
                .setNegativeButton(
                    R.string.cancel
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.create().apply {
                setOnShowListener {
                    val button: Button =
                        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                    button.setOnClickListener {
                        val frontWord = view.findViewById<EditText>(R.id.front_word_edit).text
                        val backWord =
                            view.findViewById<EditText>(R.id.back_word_edit).text
                        if (frontWord.isNotEmpty() && backWord.isNotEmpty()) {
                            (activity as? CreateWordPairCallback)?.onWordPairFragmentYes(
                                frontWord.toString(),
                                backWord.toString(),
                            )
                            dismiss()
                        } else {
                            view.findViewById<TextView>(R.id.word_list_error).visibility =
                                View.VISIBLE
                        }

                    }
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}