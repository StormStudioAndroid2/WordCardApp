package com.example.myapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordPair(
    var frontWord: String,
    var backWord: String,
    var wordPackageOwnerId: Long,
    var wordPairId: Long
) :Parcelable