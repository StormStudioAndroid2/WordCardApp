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
 *  Адаптер для списка карточек
 *  @param wordPairList - список всех карточек
 */
class WordPairListAdapter(
    var wordPairList: List<WordPair>
) :
    RecyclerView.Adapter<WordPairListAdapter.WordPairViewHolder>() {


    inner class WordPairViewHolder(view: View) : CardAnimationViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordPairViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.word_pair_item_card, parent, false)
        return WordPairViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordPairViewHolder, position: Int) {
        holder.frontWordText.text = wordPairList[position].frontWord
        holder.backWordText.text = wordPairList[position].backWord
    }

    override fun getItemCount(): Int {
        return wordPairList.size
    }

    fun changeData(dataSet: List<WordPair>) {
        this.wordPairList = dataSet
        notifyDataSetChanged()
    }
}