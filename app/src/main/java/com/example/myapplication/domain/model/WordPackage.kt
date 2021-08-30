package com.example.myapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class WordPackage(
    var id: Long,
    var name: String,
    var frontLanguage: String,
    var backLanguage: String,
    var wordPairList: List<WordPair>
) : Parcelable