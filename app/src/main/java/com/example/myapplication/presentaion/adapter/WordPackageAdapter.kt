package com.example.myapplication.presentaion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.domain.model.WordPackage

interface WordPackageAdapterCallback {
    fun onClick(adapterPosition: Int)
}

class WordPackageAdapter(
    private var dataSet: List<WordPackage>,
    private val wordPackageAdapterCallback: WordPackageAdapterCallback
) :
    RecyclerView.Adapter<WordPackageAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.language_title)
        val frontLanguageTextView: TextView = view.findViewById(R.id.language_front)
        val backLanguageTextView: TextView = view.findViewById(R.id.language_back)

        init {
            view.setOnClickListener {
                wordPackageAdapterCallback.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.word_package_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.titleTextView.text = dataSet[position].name
        viewHolder.frontLanguageTextView.text = viewHolder.itemView.context.getString(
            R.string.first_language,
            dataSet[position].frontLanguage
        )
        viewHolder.backLanguageTextView.text = viewHolder.itemView.context.getString(
            R.string.second_language,
            dataSet[position].backLanguage
        )
    }

    override fun getItemCount() = dataSet.size

    fun changeData(dataSet: List<WordPackage>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }
}