package com.example.myapplication.presentaion.adapter

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.presentaion.utils.animateWordPairCard
import com.example.myapplication.presentaion.utils.setCameraDistance

open  class CardAnimationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val frontWordView: View = view.findViewById(R.id.card_front_container)
        private val backWordView: View = view.findViewById(R.id.card_back_container)
        val frontWordText: TextView = view.findViewById(R.id.card_front)
        val backWordText: TextView = view.findViewById(R.id.card_back)
        private var isFront: Boolean = true

        private val animatorFrontSet: AnimatorSet =
            AnimatorInflater.loadAnimator(view.context, R.animator.front_animation) as AnimatorSet
        private val animatorBackSet: AnimatorSet =
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
