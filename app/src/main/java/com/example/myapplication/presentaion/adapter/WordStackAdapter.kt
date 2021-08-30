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

class WordStackAdapter(private var dataSet: List<WordPair>) :
    RecyclerView.Adapter<WordStackAdapter.WordPairCardViewHolder>() {


    inner class WordPairCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val frontWordView: View = view.findViewById(R.id.card_front_container)
        private val backWordView: View = view.findViewById(R.id.card_back_container)
        val frontWordText: TextView = view.findViewById(R.id.card_front)
        val backWordText: TextView = view.findViewById(R.id.card_back)
        private var isFront: Boolean = true
        val animatorFrontSet: AnimatorSet =
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