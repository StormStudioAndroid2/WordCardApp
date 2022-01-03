package com.example.myapplication.presentaion.adapter

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.domain.model.WordPair
import com.example.myapplication.presentaion.utils.animateWordPairCard
import com.example.myapplication.presentaion.utils.setCameraDistance

/**
 * Адаптер в виде стопки для списка всех карточек
 */
class WordStackAdapter(private var dataSet: List<WordPair>) :
    RecyclerView.Adapter<WordStackAdapter.WordPairCardViewHolder>() {


    inner class WordPairCardViewHolder(view: View) : CardAnimationViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordPairCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.word_pair_card, parent, false)
        return WordPairCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordPairCardViewHolder, position: Int) {
        holder.frontWordText.text = dataSet[position].frontWord
        holder.backWordText.text = dataSet[position].backWord
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun changeData(dataSet: List<WordPair>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }
}