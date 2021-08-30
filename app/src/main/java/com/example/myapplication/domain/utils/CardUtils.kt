package com.example.myapplication.domain.utils

//

//private lateinit var backAnimator: AnimatorSet
//private lateinit var frontAnimatorSet: AnimatorSet
//private lateinit var button: Button
//private var isFront: Boolean = true
//button = findViewById(R.id.flip_button)
//var card_front = findViewById<View>(R.id.card_front)
//var card_back = findViewById<View>(R.id.card_back)
//var scale = applicationContext.resources.displayMetrics.density
//card_front.cameraDistance = scale * 8000
//card_back.cameraDistance = scale * 8000
//backAnimator = AnimatorInflater.loadAnimator(
//applicationContext,
//R.animator.back_animation
//) as AnimatorSet
//frontAnimatorSet = AnimatorInflater.loadAnimator(
//applicationContext,
//R.animator.front_animation
//) as AnimatorSet
//button.setOnClickListener {
//    isFront = if (isFront) {
//        frontAnimatorSet.setTarget(card_front)
//        backAnimator.setTarget(card_back)
//        frontAnimatorSet.start()
//        backAnimator.start()
//        false
//    } else {
//        backAnimator.setTarget(card_front)
//        frontAnimatorSet.setTarget(card_back)
//        backAnimator.start()
//        frontAnimatorSet.start()
//        true
//    }
//}