package com.example.myapplication.presentaion.utils

import android.animation.AnimatorSet
import android.content.Context
import android.view.View

/**
 * функция, которая поворачивает карточку другой стороной
 */
fun animateWordPairCard(
    isFront: Boolean,
    animatorFrontSet: AnimatorSet,
    animatorBackSet: AnimatorSet,
    frontWordView: View,
    backWordView: View
): Boolean {
    return if (isFront) {
        animatorFrontSet.setTarget(frontWordView)
        animatorBackSet.setTarget(backWordView)
        animatorFrontSet.start()
        animatorBackSet.start()
        false
    } else {
        animatorBackSet.setTarget(frontWordView)
        animatorFrontSet.setTarget(backWordView)
        animatorBackSet.start()
        animatorFrontSet.start()
        true
    }
}

fun setCameraDistance(context: Context, view: View) {
    val distance = 8000
    val scale: Float = context.resources.displayMetrics.density * distance
    view.cameraDistance = (scale)
}