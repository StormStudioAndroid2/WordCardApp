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


class WordPairListAdapter(
    var wordPairList: List<WordPair>
) :
    RecyclerView.Adapter<WordPairListAdapter.WordPairViewHolder>() {


    inner class WordPairViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val frontWordView: View = view.findViewById(R.id.card_front_container)
        private val backWordView: View = view.findViewById(R.id.card_back_container)
        val frontWordText: TextView = view.findViewById(R.id.card_front)
        val backWordText: TextView = view.findViewById(R.id.card_back)
        private var isFront: Boolean = true
        private val animatorFrontSet: AnimatorSet =
            AnimatorInflater.loadAnimator(view.context, R.animator.front_animation) as AnimatorSet
        val animatorBackSet: AnimatorSet =
            AnimatorInflater.loadAnimator(view.context, R.animator.back_animation) as AnimatorSet


        init {
            setCameraDistance(view.context, view)
            view.setOnClickListener {
                isFront = animateWordPairCard(
                    isFront,
                    animatorFrontSet,
                    animatorBackSet,
                    frontWordView,
                    backWordView
                )
            }
        }

    }

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